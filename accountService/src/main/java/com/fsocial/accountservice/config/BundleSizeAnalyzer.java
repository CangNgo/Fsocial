package com.fsocial.accountservice.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Bundle Size Analyzer for FSocial Account Service
 * Analyzes JAR files and dependencies to provide size metrics and optimization recommendations
 */
public class BundleSizeAnalyzer {
    
    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    
    public static void main(String[] args) {
        String targetDir = args.length > 0 ? args[0] : "target";
        String dependenciesDir = args.length > 1 ? args[1] : "target/dependencies";
        
        System.out.println("\n" + ANSI_BLUE + "Starting Bundle Size Analysis..." + ANSI_RESET);
        analyzeBundles(targetDir, dependenciesDir);
    }
    
    public static void analyzeBundles(String targetDir, String dependenciesDir) {
        printHeader("FSocial Account Service - Bundle Size Analysis Report");
        
        // Analyze main artifact
        long mainArtifactSize = analyzeMainArtifact(targetDir);
        
        // Analyze dependencies
        Map<String, Long> dependencySizes = analyzeDependencies(dependenciesDir);
        
        // Generate comprehensive report
        generateSummaryReport(mainArtifactSize, dependencySizes);
        
        // Generate recommendations
        generateRecommendations(dependenciesDir, dependencySizes);
        
        // Generate HTML report
        try {
            generateHtmlReport(targetDir, dependenciesDir, mainArtifactSize, dependencySizes);
            System.out.println("\n" + ANSI_GREEN + "✅ HTML Report generated: target/reports/bundle-analyzer.html" + ANSI_RESET);
        } catch (IOException e) {
            System.err.println(ANSI_RED + "❌ Error generating HTML report: " + e.getMessage() + ANSI_RESET);
        }
        
        printFooter();
    }
    
    private static long analyzeMainArtifact(String targetDir) {
        System.out.println("\n" + ANSI_CYAN + "📦 MAIN ARTIFACT ANALYSIS" + ANSI_RESET);
        printSeparator();
        
        File target = new File(targetDir);
        File[] jars = target.listFiles((dir, name) -> 
            name.endsWith(".jar") && 
            !name.endsWith("-sources.jar") && 
            !name.endsWith("-javadoc.jar") &&
            !name.contains("original")
        );
        
        if (jars == null || jars.length == 0) {
            System.out.println(ANSI_YELLOW + "⚠️  No JAR files found in target directory" + ANSI_RESET);
            return 0;
        }
        
        long totalSize = 0;
        for (File jar : jars) {
            long size = analyzeJarFile(jar, true);
            totalSize += size;
        }
        
        return totalSize;
    }
    
    private static Map<String, Long> analyzeDependencies(String dependenciesDir) {
        System.out.println("\n" + ANSI_CYAN + "📚 DEPENDENCIES ANALYSIS" + ANSI_RESET);
        printSeparator();
        
        File depsDir = new File(dependenciesDir);
        Map<String, Long> dependencySizes = new LinkedHashMap<>();
        
        if (!depsDir.exists()) {
            System.out.println(ANSI_YELLOW + "⚠️  Dependencies directory not found." + ANSI_RESET);
            System.out.println("   Run: mvn dependency:copy-dependencies");
            return dependencySizes;
        }
        
        File[] deps = depsDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (deps == null || deps.length == 0) {
            System.out.println(ANSI_YELLOW + "⚠️  No dependency JARs found" + ANSI_RESET);
            return dependencySizes;
        }
        
        // Sort by size (largest first)
        List<File> sortedDeps = Arrays.stream(deps)
            .sorted((a, b) -> Long.compare(b.length(), a.length()))
            .collect(Collectors.toList());
        
        long totalSize = sortedDeps.stream().mapToLong(File::length).sum();
        
        // Print header
        System.out.printf(ANSI_BLUE + "%-70s %15s %10s%n" + ANSI_RESET, 
            "Dependency", "Size", "% of Total");
        printSeparator();
        
        // Display each dependency
        int count = 0;
        for (File dep : sortedDeps) {
            count++;
            long size = dep.length();
            double percentage = (size * 100.0) / totalSize;
            
            String color = getColorBySize(size);
            dependencySizes.put(dep.getName(), size);
            
            System.out.printf(color + "%-70s %15s %9.2f%%" + ANSI_RESET + "%n", 
                truncateName(dep.getName(), 70),
                formatSize(size),
                percentage
            );
            
            // Show top 30 by default
            if (count == 30 && sortedDeps.size() > 30) {
                System.out.println(ANSI_YELLOW + "... (" + (sortedDeps.size() - 30) + 
                    " more dependencies - see target/reports/dependency-list.txt for full list)" + ANSI_RESET);
                break;
            }
        }
        
        printSeparator();
        System.out.printf(ANSI_GREEN + "%-70s %15s%n" + ANSI_RESET, 
            "TOTAL DEPENDENCIES SIZE", formatSize(totalSize));
        System.out.printf(ANSI_GREEN + "%-70s %15d%n" + ANSI_RESET, 
            "NUMBER OF DEPENDENCIES", sortedDeps.size());
        
        return dependencySizes;
    }
    
    private static long analyzeJarFile(File jarFile, boolean detailed) {
        try (JarFile jar = new JarFile(jarFile)) {
            long jarSize = jarFile.length();
            
            System.out.println("\n" + ANSI_GREEN + "📄 " + jarFile.getName() + ANSI_RESET);
            System.out.println("   Compressed Size: " + ANSI_YELLOW + formatSize(jarSize) + ANSI_RESET);
            
            if (detailed) {
                Map<String, Long> packageSizes = new TreeMap<>();
                long totalUncompressedSize = 0;
                int classCount = 0;
                int resourceCount = 0;
                
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        String name = entry.getName();
                        long size = entry.getSize();
                        totalUncompressedSize += size;
                        
                        if (name.endsWith(".class")) {
                            classCount++;
                        } else {
                            resourceCount++;
                        }
                        
                        // Group by package
                        String packageName = extractPackageName(name);
                        packageSizes.merge(packageName, size, Long::sum);
                    }
                }
                
                System.out.println("   Uncompressed Size: " + formatSize(totalUncompressedSize));
                System.out.println("   Compression Ratio: " + 
                    String.format("%.2f%%", (jarSize * 100.0) / totalUncompressedSize));
                System.out.println("   Classes: " + classCount);
                System.out.println("   Resources: " + resourceCount);
                
                // Show top packages
                System.out.println("\n   " + ANSI_BLUE + "Top 10 Packages by Size:" + ANSI_RESET);
                packageSizes.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .forEach(entry -> 
                        System.out.printf("      %-60s %15s%n", 
                            truncateName(entry.getKey(), 60), 
                            formatSize(entry.getValue()))
                    );
            }
            
            return jarSize;
        } catch (IOException e) {
            System.err.println(ANSI_RED + "   ❌ Error analyzing JAR: " + e.getMessage() + ANSI_RESET);
            return 0;
        }
    }
    
    private static void generateSummaryReport(long mainArtifactSize, Map<String, Long> dependencySizes) {
        System.out.println("\n" + ANSI_CYAN + "📊 SUMMARY REPORT" + ANSI_RESET);
        printSeparator();
        
        long totalDepsSize = dependencySizes.values().stream().mapToLong(Long::longValue).sum();
        long totalSize = mainArtifactSize + totalDepsSize;
        
        System.out.printf("%-50s %20s%n", "Main Artifact Size:", formatSize(mainArtifactSize));
        System.out.printf("%-50s %20s%n", "All Dependencies Size:", formatSize(totalDepsSize));
        System.out.printf("%-50s %20s%n", "Total Application Size:", ANSI_GREEN + formatSize(totalSize) + ANSI_RESET);
        
        if (totalSize > 100 * MB) {
            System.out.println("\n" + ANSI_YELLOW + "⚠️  WARNING: Total application size exceeds 100MB!" + ANSI_RESET);
        }
        
        // Category breakdown
        System.out.println("\n" + ANSI_BLUE + "Dependencies by Category:" + ANSI_RESET);
        categorizeDependencies(dependencySizes);
    }
    
    private static void categorizeDependencies(Map<String, Long> dependencySizes) {
        Map<String, Long> categories = new HashMap<>();
        
        dependencySizes.forEach((name, size) -> {
            String category = categorizeByName(name);
            categories.merge(category, size, Long::sum);
        });
        
        categories.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("   %-30s %20s%n", entry.getKey(), formatSize(entry.getValue()))
            );
    }
    
    private static String categorizeByName(String name) {
        if (name.contains("spring")) return "Spring Framework";
        if (name.contains("jackson")) return "Jackson (JSON)";
        if (name.contains("tomcat") || name.contains("undertow")) return "Web Server";
        if (name.contains("hibernate") || name.contains("jpa")) return "JPA/Hibernate";
        if (name.contains("security") || name.contains("oauth")) return "Security/OAuth";
        if (name.contains("kafka")) return "Kafka";
        if (name.contains("redis") || name.contains("lettuce")) return "Redis";
        if (name.contains("mysql") || name.contains("jdbc")) return "Database Drivers";
        if (name.contains("jwt") || name.contains("jjwt")) return "JWT";
        if (name.contains("logging") || name.contains("slf4j") || name.contains("logback")) return "Logging";
        if (name.contains("validation")) return "Validation";
        if (name.contains("mail")) return "Email";
        if (name.contains("openapi") || name.contains("swagger")) return "API Documentation";
        return "Other";
    }
    
    private static void generateRecommendations(String dependenciesDir, Map<String, Long> dependencySizes) {
        System.out.println("\n" + ANSI_CYAN + "💡 OPTIMIZATION RECOMMENDATIONS" + ANSI_RESET);
        printSeparator();
        
        File depsDir = new File(dependenciesDir);
        if (!depsDir.exists()) return;
        
        // Check for large dependencies
        List<Map.Entry<String, Long>> largeDeps = dependencySizes.entrySet().stream()
            .filter(e -> e.getValue() > 5 * MB)
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toList());
        
        if (!largeDeps.isEmpty()) {
            System.out.println("\n" + ANSI_YELLOW + "⚠️  Large Dependencies (>5MB):" + ANSI_RESET);
            for (Map.Entry<String, Long> dep : largeDeps) {
                System.out.println("   • " + dep.getKey() + " (" + ANSI_RED + formatSize(dep.getValue()) + ANSI_RESET + ")");
                provideSpecificRecommendation(dep.getKey());
            }
        }
        
        // Check for potential duplicates
        checkDuplicateDependencies(dependencySizes);
        
        // General recommendations
        System.out.println("\n" + ANSI_GREEN + "📋 General Optimization Tips:" + ANSI_RESET);
        System.out.println("   1. ✓ Use Maven dependency:analyze to find unused dependencies");
        System.out.println("   2. ✓ Consider using spring-boot-starter-webflux instead of web for reactive apps");
        System.out.println("   3. ✓ Exclude unnecessary transitive dependencies");
        System.out.println("   4. ✓ Use scope='provided' for dependencies available in runtime environment");
        System.out.println("   5. ✓ Enable Spring Boot layered JARs for better Docker caching");
        System.out.println("   6. ✓ Consider splitting into microservices if the app is too large");
        System.out.println("   7. ✓ Remove spring-boot-devtools from production builds");
        System.out.println("   8. ✓ Use ProGuard or R8 for aggressive size reduction");
        
        System.out.println("\n" + ANSI_BLUE + "📁 Generated Reports:" + ANSI_RESET);
        System.out.println("   • target/reports/dependency-tree.txt - Full dependency hierarchy");
        System.out.println("   • target/reports/dependency-list.txt - All dependencies listed");
        System.out.println("   • target/dependency-analysis.xml - Unused dependency analysis");
        System.out.println("   • target/dependencies/ - All dependency JAR files");
    }
    
    private static void provideSpecificRecommendation(String depName) {
        if (depName.contains("spring-boot-starter-web")) {
            System.out.println("     → Consider using reactive stack (WebFlux) for smaller footprint");
        } else if (depName.contains("jackson")) {
            System.out.println("     → Check if all Jackson modules are necessary");
        } else if (depName.contains("hibernate")) {
            System.out.println("     → Consider using Spring Data JDBC for simpler use cases");
        } else if (depName.contains("tomcat")) {
            System.out.println("     → Consider switching to Undertow (smaller embedded server)");
        } else {
            System.out.println("     → Review if this dependency is fully utilized");
        }
    }
    
    private static void checkDuplicateDependencies(Map<String, Long> dependencySizes) {
        Map<String, List<String>> baseNameGroups = dependencySizes.keySet().stream()
            .collect(Collectors.groupingBy(name -> 
                name.replaceAll("-\\d+.*\\.jar$", "")
            ));
        
        List<Map.Entry<String, List<String>>> duplicates = baseNameGroups.entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .collect(Collectors.toList());
        
        if (!duplicates.isEmpty()) {
            System.out.println("\n" + ANSI_RED + "⚠️  Potential Duplicate Dependencies (Multiple Versions):" + ANSI_RESET);
            for (Map.Entry<String, List<String>> entry : duplicates) {
                System.out.println("   • " + entry.getKey());
                entry.getValue().forEach(v -> System.out.println("     - " + v));
                System.out.println("     → Use dependencyManagement to resolve version conflicts");
            }
        }
    }
    
    private static String extractPackageName(String path) {
        if (path.startsWith("META-INF/")) return "META-INF";
        if (path.startsWith("BOOT-INF/")) return "BOOT-INF";
        if (path.endsWith(".class")) {
            int lastSlash = path.lastIndexOf('/');
            return lastSlash > 0 ? path.substring(0, lastSlash).replace('/', '.') : "(default)";
        }
        return "(resources)";
    }
    
    private static String formatSize(long size) {
        if (size >= MB) {
            return String.format("%.2f MB", size / (double) MB);
        } else if (size >= KB) {
            return String.format("%.2f KB", size / (double) KB);
        } else {
            return size + " B";
        }
    }
    
    private static String truncateName(String name, int maxLength) {
        if (name.length() <= maxLength) return name;
        return "..." + name.substring(name.length() - maxLength + 3);
    }
    
    private static String getColorBySize(long size) {
        if (size > 10 * MB) return ANSI_RED;
        if (size > 5 * MB) return ANSI_YELLOW;
        return "";
    }
    
    private static void printHeader(String title) {
        System.out.println("\n" + ANSI_CYAN + "=".repeat(80) + ANSI_RESET);
        System.out.println(ANSI_CYAN + centerText(title, 80) + ANSI_RESET);
        System.out.println(ANSI_CYAN + "=".repeat(80) + ANSI_RESET);
    }
    
    private static void printSeparator() {
        System.out.println("-".repeat(80));
    }
    
    private static void printFooter() {
        System.out.println("\n" + ANSI_CYAN + "=".repeat(80) + ANSI_RESET);
        System.out.println(ANSI_GREEN + centerText("Analysis Complete ✓", 80) + ANSI_RESET);
        System.out.println(ANSI_CYAN + "=".repeat(80) + ANSI_RESET + "\n");
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    
    private static void generateHtmlReport(String targetDir, String dependenciesDir, 
                                          long mainArtifactSize, Map<String, Long> dependencySizes) throws IOException {
        File reportsDir = new File(targetDir, "reports");
        reportsDir.mkdirs();
        
        File htmlFile = new File(reportsDir, "bundle-analyzer.html");
        
        long totalDepsSize = dependencySizes.values().stream().mapToLong(Long::longValue).sum();
        long totalSize = mainArtifactSize + totalDepsSize;
        
        // Prepare dependency data for JSON
        List<Map<String, Object>> depsList = new ArrayList<>();
        List<File> sortedDeps = new ArrayList<>();
        File depsDir = new File(dependenciesDir);
        if (depsDir.exists()) {
            File[] deps = depsDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (deps != null) {
                sortedDeps = Arrays.stream(deps)
                    .sorted((a, b) -> Long.compare(b.length(), a.length()))
                    .collect(Collectors.toList());
                
                for (File dep : sortedDeps) {
                    long size = dep.length();
                    double percentage = (size * 100.0) / totalDepsSize;
                    Map<String, Object> depData = new HashMap<>();
                    depData.put("name", dep.getName());
                    depData.put("size", size);
                    depData.put("sizeFormatted", formatSize(size));
                    depData.put("percentage", percentage);
                    depData.put("category", categorizeByName(dep.getName()));
                    depData.put("color", getHtmlColorBySize(size));
                    depsList.add(depData);
                }
            }
        }
        
        // Category breakdown
        Map<String, Long> categories = new HashMap<>();
        dependencySizes.forEach((name, size) -> {
            String category = categorizeByName(name);
            categories.merge(category, size, Long::sum);
        });
        
        List<Map<String, Object>> categoryList = categories.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .map(entry -> {
                Map<String, Object> catData = new HashMap<>();
                catData.put("name", entry.getKey());
                catData.put("size", entry.getValue());
                catData.put("sizeFormatted", formatSize(entry.getValue()));
                catData.put("percentage", (entry.getValue() * 100.0) / totalDepsSize);
                return catData;
            })
            .collect(Collectors.toList());
        
        // Generate HTML
        String html = generateHtmlContent(mainArtifactSize, totalDepsSize, totalSize, 
                                         depsList, categoryList, sortedDeps.size());
        
        Files.write(htmlFile.toPath(), html.getBytes());
    }
    
    private static String generateHtmlContent(long mainArtifactSize, long totalDepsSize, 
                                             long totalSize, List<Map<String, Object>> depsList,
                                             List<Map<String, Object>> categoryList, int depCount) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Bundle Analyzer - Account Service</title>\n");
        html.append("    <script src=\"https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js\"></script>\n");
        html.append("    <style>\n");
        html.append(getCssStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <header>\n");
        html.append("            <h1>📦 Bundle Analyzer</h1>\n");
        html.append("            <p class=\"subtitle\">FSocial Account Service - Dependency Size Analysis</p>\n");
        html.append("        </header>\n");
        
        // Summary Section
        html.append("        <section class=\"summary\">\n");
        html.append("            <h2>📊 Summary</h2>\n");
        html.append("            <div class=\"summary-grid\">\n");
        html.append("                <div class=\"summary-card\">\n");
        html.append("                    <div class=\"summary-label\">Main Artifact</div>\n");
        html.append("                    <div class=\"summary-value\">").append(formatSize(mainArtifactSize)).append("</div>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"summary-card\">\n");
        html.append("                    <div class=\"summary-label\">Dependencies</div>\n");
        html.append("                    <div class=\"summary-value\">").append(formatSize(totalDepsSize)).append("</div>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"summary-card highlight\">\n");
        html.append("                    <div class=\"summary-label\">Total Size</div>\n");
        html.append("                    <div class=\"summary-value\">").append(formatSize(totalSize)).append("</div>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"summary-card\">\n");
        html.append("                    <div class=\"summary-label\">Dependency Count</div>\n");
        html.append("                    <div class=\"summary-value\">").append(depCount).append("</div>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </section>\n");
        
        // Category Breakdown with Chart
        html.append("        <section class=\"categories\">\n");
        html.append("            <div class=\"section-header\">\n");
        html.append("                <h2>📚 Dependencies by Category</h2>\n");
        html.append("                <div class=\"view-controls\">\n");
        html.append("                    <button class=\"view-btn active\" data-view=\"list\">📋 List</button>\n");
        html.append("                    <button class=\"view-btn\" data-view=\"chart\">📊 Chart</button>\n");
        html.append("                    <button class=\"view-btn\" data-view=\"treemap\">🗺️ Treemap</button>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"category-list\" id=\"categoryListView\">\n");
        for (Map<String, Object> cat : categoryList) {
            double percentage = (Double) cat.get("percentage");
            html.append("                <div class=\"category-item\" data-category=\"").append(escapeHtml((String) cat.get("name"))).append("\">\n");
            html.append("                    <div class=\"category-name\">").append(cat.get("name")).append("</div>\n");
            html.append("                    <div class=\"category-bar\">\n");
            html.append("                        <div class=\"category-fill\" style=\"width: ").append(percentage).append("%\"></div>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"category-size\">").append(cat.get("sizeFormatted")).append(" (").append(String.format("%.2f", percentage)).append("%)</div>\n");
            html.append("                </div>\n");
        }
        html.append("            </div>\n");
        html.append("            <div class=\"chart-container\" id=\"categoryChartView\" style=\"display: none;\">\n");
        html.append("                <canvas id=\"categoryChart\"></canvas>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"treemap-container\" id=\"categoryTreemapView\" style=\"display: none;\">\n");
        html.append("                <div id=\"treemap\"></div>\n");
        html.append("            </div>\n");
        html.append("        </section>\n");
        
        // Search and Filter with Advanced Controls
        html.append("        <section class=\"dependencies\">\n");
        html.append("            <div class=\"section-header\">\n");
        html.append("                <h2>🔍 Dependencies</h2>\n");
        html.append("                <div class=\"controls-group\">\n");
        html.append("                    <input type=\"text\" id=\"searchInput\" placeholder=\"Search dependencies...\" class=\"search-input\">\n");
        html.append("                    <select id=\"sortSelect\" class=\"sort-select\">\n");
        html.append("                        <option value=\"size-desc\">Size ↓</option>\n");
        html.append("                        <option value=\"size-asc\">Size ↑</option>\n");
        html.append("                        <option value=\"name-asc\">Name A-Z</option>\n");
        html.append("                        <option value=\"name-desc\">Name Z-A</option>\n");
        html.append("                        <option value=\"category-asc\">Category A-Z</option>\n");
        html.append("                    </select>\n");
        html.append("                    <button id=\"exportBtn\" class=\"export-btn\">💾 Export JSON</button>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"filter-section\">\n");
        html.append("                <div class=\"filter-label\">Filter by Category:</div>\n");
        html.append("                <div class=\"filter-checkboxes\" id=\"categoryFilters\">\n");
        Set<String> uniqueCategories = new HashSet<>();
        for (Map<String, Object> dep : depsList) {
            uniqueCategories.add((String) dep.get("category"));
        }
        for (String category : uniqueCategories.stream().sorted().collect(Collectors.toList())) {
            html.append("                    <label class=\"filter-checkbox\">\n");
            html.append("                        <input type=\"checkbox\" value=\"").append(escapeHtml(category)).append("\" checked>\n");
            html.append("                        <span>").append(escapeHtml(category)).append("</span>\n");
            html.append("                    </label>\n");
        }
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"dependency-list\" id=\"dependencyList\">\n");
        
        for (Map<String, Object> dep : depsList) {
            double percentage = (Double) dep.get("percentage");
            String color = (String) dep.get("color");
            long size = ((Number) dep.get("size")).longValue();
            html.append("                <div class=\"dependency-item\" data-name=\"").append(escapeHtml((String) dep.get("name"))).append("\" data-category=\"").append(escapeHtml((String) dep.get("category"))).append("\" data-size=\"").append(size).append("\" title=\"").append(escapeHtml((String) dep.get("name"))).append(" - ").append(dep.get("sizeFormatted")).append(" (").append(String.format("%.2f", percentage)).append("%)\">\n");
            html.append("                    <div class=\"dep-name\" title=\"").append(escapeHtml((String) dep.get("name"))).append("\">").append(escapeHtml((String) dep.get("name"))).append("</div>\n");
            html.append("                    <div class=\"dep-bar\">\n");
            html.append("                        <div class=\"dep-fill\" style=\"width: ").append(percentage).append("%; background-color: ").append(color).append("\" title=\"").append(dep.get("sizeFormatted")).append("\"></div>\n");
            html.append("                    </div>\n");
            html.append("                    <div class=\"dep-size\">").append(dep.get("sizeFormatted")).append(" <span class=\"dep-percentage\">(").append(String.format("%.2f", percentage)).append("%)</span></div>\n");
            html.append("                </div>\n");
        }
        
        html.append("            </div>\n");
        html.append("        </section>\n");
        
        html.append("    </div>\n");
        html.append("    <script>\n");
        html.append("        // Embed data for JavaScript\n");
        html.append("        const bundleData = {\n");
        html.append("            mainArtifactSize: ").append(mainArtifactSize).append(",\n");
        html.append("            totalDepsSize: ").append(totalDepsSize).append(",\n");
        html.append("            totalSize: ").append(totalSize).append(",\n");
        html.append("            depCount: ").append(depCount).append(",\n");
        html.append("            dependencies: ").append(convertToJson(depsList)).append(",\n");
        html.append("            categories: ").append(convertToJson(categoryList)).append("\n");
        html.append("        };\n");
        html.append("    </script>\n");
        html.append("    <script>\n");
        html.append(getJavaScript());
        html.append("    </script>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private static String convertToJson(List<Map<String, Object>> data) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> item = data.get(i);
            json.append("                {");
            boolean first = true;
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                if (!first) json.append(", ");
                json.append("\"").append(entry.getKey()).append("\": ");
                Object value = entry.getValue();
                if (value instanceof String) {
                    json.append("\"").append(escapeJson((String) value)).append("\"");
                } else if (value instanceof Number) {
                    json.append(value);
                } else {
                    json.append("\"").append(escapeJson(String.valueOf(value))).append("\"");
                }
                first = false;
            }
            json.append("}");
            if (i < data.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("            ]");
        return json.toString();
    }
    
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private static String getCssStyles() {
        return """
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            
            body {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
                color: #333;
            }
            
            .container {
                max-width: 1200px;
                margin: 0 auto;
                background: white;
                border-radius: 12px;
                box-shadow: 0 20px 60px rgba(0,0,0,0.3);
                overflow: hidden;
            }
            
            header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 40px;
                text-align: center;
            }
            
            header h1 {
                font-size: 2.5em;
                margin-bottom: 10px;
            }
            
            .subtitle {
                opacity: 0.9;
                font-size: 1.1em;
            }
            
            section {
                padding: 30px 40px;
                border-bottom: 1px solid #eee;
            }
            
            section:last-child {
                border-bottom: none;
            }
            
            h2 {
                font-size: 1.8em;
                margin-bottom: 20px;
                color: #333;
            }
            
            .summary-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
            }
            
            .summary-card {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                text-align: center;
                border: 2px solid #e9ecef;
            }
            
            .summary-card.highlight {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
            }
            
            .summary-label {
                font-size: 0.9em;
                opacity: 0.7;
                margin-bottom: 10px;
            }
            
            .summary-value {
                font-size: 1.8em;
                font-weight: bold;
            }
            
            .category-list {
                display: flex;
                flex-direction: column;
                gap: 15px;
            }
            
            .category-item {
                display: flex;
                align-items: center;
                gap: 15px;
            }
            
            .category-name {
                min-width: 200px;
                font-weight: 500;
            }
            
            .category-bar {
                flex: 1;
                height: 30px;
                background: #e9ecef;
                border-radius: 15px;
                overflow: hidden;
            }
            
            .category-fill {
                height: 100%;
                background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
                transition: width 0.3s ease;
            }
            
            .category-size {
                min-width: 120px;
                text-align: right;
                font-weight: 500;
            }
            
            .section-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                flex-wrap: wrap;
                gap: 15px;
            }
            
            .view-controls {
                display: flex;
                gap: 10px;
            }
            
            .view-btn {
                padding: 8px 16px;
                border: 2px solid #667eea;
                background: white;
                color: #667eea;
                border-radius: 6px;
                cursor: pointer;
                font-size: 0.9em;
                transition: all 0.3s;
            }
            
            .view-btn:hover {
                background: #f0f0ff;
            }
            
            .view-btn.active {
                background: #667eea;
                color: white;
            }
            
            .controls-group {
                display: flex;
                gap: 10px;
                align-items: center;
                flex-wrap: wrap;
            }
            
            .search-input, .sort-select {
                padding: 10px 15px;
                border: 2px solid #e9ecef;
                border-radius: 8px;
                font-size: 1em;
                transition: border-color 0.3s;
            }
            
            .search-input {
                width: 300px;
            }
            
            .sort-select {
                min-width: 150px;
            }
            
            .search-input:focus, .sort-select:focus {
                outline: none;
                border-color: #667eea;
            }
            
            .export-btn {
                padding: 10px 20px;
                background: #28a745;
                color: white;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 1em;
                transition: background 0.3s;
            }
            
            .export-btn:hover {
                background: #218838;
            }
            
            .filter-section {
                margin-bottom: 20px;
                padding: 15px;
                background: #f8f9fa;
                border-radius: 8px;
            }
            
            .filter-label {
                font-weight: 600;
                margin-bottom: 10px;
                color: #495057;
            }
            
            .filter-checkboxes {
                display: flex;
                flex-wrap: wrap;
                gap: 15px;
            }
            
            .filter-checkbox {
                display: flex;
                align-items: center;
                gap: 8px;
                cursor: pointer;
                padding: 5px 10px;
                border-radius: 6px;
                transition: background 0.2s;
            }
            
            .filter-checkbox:hover {
                background: #e9ecef;
            }
            
            .filter-checkbox input[type="checkbox"] {
                cursor: pointer;
                width: 18px;
                height: 18px;
            }
            
            .chart-container {
                margin-top: 20px;
                height: 400px;
                position: relative;
            }
            
            .treemap-container {
                margin-top: 20px;
                min-height: 500px;
            }
            
            #treemap {
                display: flex;
                flex-wrap: wrap;
                gap: 2px;
            }
            
            .treemap-item {
                position: relative;
                border: 2px solid white;
                cursor: pointer;
                transition: transform 0.2s, z-index 0.2s;
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-weight: 600;
                font-size: 0.85em;
                text-align: center;
                padding: 5px;
                overflow: hidden;
            }
            
            .treemap-item:hover {
                transform: scale(1.05);
                z-index: 10;
                box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            }
            
            .treemap-label {
                position: absolute;
                bottom: 5px;
                left: 5px;
                right: 5px;
                font-size: 0.75em;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
            }
            
            .dependency-list {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }
            
            .dependency-item {
                display: flex;
                align-items: center;
                gap: 15px;
                padding: 15px;
                background: #f8f9fa;
                border-radius: 8px;
                transition: transform 0.2s, box-shadow 0.2s;
            }
            
            .dependency-item:hover {
                transform: translateX(5px);
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }
            
            .dep-name {
                min-width: 400px;
                font-family: 'Courier New', monospace;
                font-size: 0.9em;
                word-break: break-all;
            }
            
            .dep-bar {
                flex: 1;
                height: 25px;
                background: #e9ecef;
                border-radius: 12px;
                overflow: hidden;
            }
            
            .dep-fill {
                height: 100%;
                transition: width 0.3s ease;
            }
            
            .dep-size {
                min-width: 120px;
                text-align: right;
                font-weight: 500;
            }
            
            .dep-percentage {
                opacity: 0.7;
                font-size: 0.9em;
            }
            
            .hidden {
                display: none !important;
            }
            
            .tooltip {
                position: absolute;
                background: rgba(0, 0, 0, 0.9);
                color: white;
                padding: 8px 12px;
                border-radius: 6px;
                font-size: 0.85em;
                pointer-events: none;
                z-index: 1000;
                max-width: 300px;
                word-wrap: break-word;
            }
            
            @media (max-width: 768px) {
                .summary-grid {
                    grid-template-columns: 1fr;
                }
                
                .category-item,
                .dependency-item {
                    flex-direction: column;
                    align-items: flex-start;
                }
                
                .category-name,
                .dep-name {
                    min-width: auto;
                    width: 100%;
                }
                
                .category-bar,
                .dep-bar {
                    width: 100%;
                }
                
                .section-header {
                    flex-direction: column;
                    align-items: flex-start;
                }
                
                .search-input {
                    width: 100%;
                }
            }
            """;
    }
    
    private static String getJavaScript() {
        return """
            // Initialize
            let currentView = 'list';
            let categoryChart = null;
            
            // Search functionality
            document.getElementById('searchInput').addEventListener('input', function(e) {
                filterAndSort();
            });
            
            // Sort functionality
            document.getElementById('sortSelect').addEventListener('change', function(e) {
                filterAndSort();
            });
            
            // Category filter functionality
            document.querySelectorAll('#categoryFilters input[type="checkbox"]').forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    filterAndSort();
                });
            });
            
            // View mode switching
            document.querySelectorAll('.view-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    document.querySelectorAll('.view-btn').forEach(b => b.classList.remove('active'));
                    this.classList.add('active');
                    currentView = this.getAttribute('data-view');
                    showView(currentView);
                });
            });
            
            // Export functionality
            document.getElementById('exportBtn').addEventListener('click', function() {
                const dataStr = JSON.stringify(bundleData, null, 2);
                const dataBlob = new Blob([dataStr], {type: 'application/json'});
                const url = URL.createObjectURL(dataBlob);
                const link = document.createElement('a');
                link.href = url;
                link.download = 'bundle-analysis.json';
                link.click();
                URL.revokeObjectURL(url);
            });
            
            function filterAndSort() {
                const searchTerm = document.getElementById('searchInput').value.toLowerCase();
                const sortValue = document.getElementById('sortSelect').value;
                const checkedCategories = Array.from(document.querySelectorAll('#categoryFilters input[type="checkbox"]:checked'))
                    .map(cb => cb.value.toLowerCase());
                
                const items = Array.from(document.querySelectorAll('.dependency-item'));
                
                items.forEach(item => {
                    const name = item.getAttribute('data-name').toLowerCase();
                    const category = item.getAttribute('data-category').toLowerCase();
                    const matchesSearch = !searchTerm || name.includes(searchTerm) || category.includes(searchTerm);
                    const matchesCategory = checkedCategories.length === 0 || checkedCategories.includes(category);
                    
                    if (matchesSearch && matchesCategory) {
                        item.classList.remove('hidden');
                    } else {
                        item.classList.add('hidden');
                    }
                });
                
                // Sort visible items
                const visibleItems = items.filter(item => !item.classList.contains('hidden'));
                const container = document.getElementById('dependencyList');
                
                const [sortBy, order] = sortValue.split('-');
                visibleItems.sort((a, b) => {
                    let comparison = 0;
                    if (sortBy === 'size') {
                        comparison = parseInt(a.getAttribute('data-size')) - parseInt(b.getAttribute('data-size'));
                    } else if (sortBy === 'name') {
                        comparison = a.getAttribute('data-name').localeCompare(b.getAttribute('data-name'));
                    } else if (sortBy === 'category') {
                        comparison = a.getAttribute('data-category').localeCompare(b.getAttribute('data-category'));
                    }
                    return order === 'desc' ? -comparison : comparison;
                });
                
                visibleItems.forEach(item => container.appendChild(item));
            }
            
            function showView(view) {
                document.getElementById('categoryListView').style.display = view === 'list' ? 'flex' : 'none';
                document.getElementById('categoryChartView').style.display = view === 'chart' ? 'block' : 'none';
                document.getElementById('categoryTreemapView').style.display = view === 'treemap' ? 'block' : 'none';
                
                if (view === 'chart') {
                    initCategoryChart();
                } else if (view === 'treemap') {
                    initTreemap();
                }
            }
            
            function initCategoryChart() {
                const ctx = document.getElementById('categoryChart');
                if (categoryChart) {
                    categoryChart.destroy();
                }
                
                categoryChart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: bundleData.categories.map(c => c.name),
                        datasets: [{
                            data: bundleData.categories.map(c => c.size),
                            backgroundColor: [
                                '#667eea', '#764ba2', '#f093fb', '#4facfe',
                                '#00f2fe', '#43e97b', '#fa709a', '#fee140',
                                '#30cfd0', '#a8edea', '#fed6e3', '#ffecd2'
                            ]
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'right',
                            },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        const category = bundleData.categories[context.dataIndex];
                                        return category.name + ': ' + category.sizeFormatted + ' (' + category.percentage.toFixed(2) + '%)';
                                    }
                                }
                            }
                        }
                    }
                });
            }
            
            function initTreemap() {
                const container = document.getElementById('treemap');
                container.innerHTML = '';
                
                const totalSize = bundleData.categories.reduce((sum, c) => sum + c.size, 0);
                const containerWidth = container.offsetWidth;
                const containerHeight = 500;
                
                bundleData.categories.forEach(category => {
                    const percentage = (category.size / totalSize) * 100;
                    const width = Math.max(containerWidth * (percentage / 100), 100);
                    const height = Math.max(containerHeight * 0.15, 80);
                    
                    const item = document.createElement('div');
                    item.className = 'treemap-item';
                    item.style.width = width + 'px';
                    item.style.height = height + 'px';
                    item.style.backgroundColor = getCategoryColor(category.name);
                    item.title = category.name + ': ' + category.sizeFormatted + ' (' + category.percentage.toFixed(2) + '%)';
                    
                    const label = document.createElement('div');
                    label.className = 'treemap-label';
                    label.textContent = category.name + ' (' + category.sizeFormatted + ')';
                    item.appendChild(label);
                    
                    item.addEventListener('click', function() {
                        // Filter dependencies by category
                        document.querySelectorAll('#categoryFilters input[type="checkbox"]').forEach(cb => {
                            cb.checked = cb.value === category.name;
                        });
                        document.getElementById('searchInput').value = '';
                        filterAndSort();
                    });
                    
                    container.appendChild(item);
                });
            }
            
            function getCategoryColor(categoryName) {
                const colors = {
                    'Spring Framework': '#667eea',
                    'JPA/Hibernate': '#764ba2',
                    'Jackson (JSON)': '#f093fb',
                    'Web Server': '#4facfe',
                    'Security/OAuth': '#00f2fe',
                    'Kafka': '#43e97b',
                    'Redis': '#fa709a',
                    'Database Drivers': '#fee140',
                    'JWT': '#30cfd0',
                    'Logging': '#a8edea',
                    'Validation': '#fed6e3',
                    'Email': '#ffecd2',
                    'API Documentation': '#667eea',
                    'Other': '#95a5a6'
                };
                return colors[categoryName] || '#95a5a6';
            }
            
            // Tooltip functionality
            document.querySelectorAll('.dependency-item').forEach(item => {
                item.addEventListener('mouseenter', function(e) {
                    const tooltip = document.createElement('div');
                    tooltip.className = 'tooltip';
                    tooltip.textContent = this.getAttribute('title');
                    document.body.appendChild(tooltip);
                    
                    const rect = this.getBoundingClientRect();
                    tooltip.style.left = (rect.left + rect.width / 2 - tooltip.offsetWidth / 2) + 'px';
                    tooltip.style.top = (rect.top - tooltip.offsetHeight - 10) + 'px';
                    
                    this._tooltip = tooltip;
                });
                
                item.addEventListener('mouseleave', function() {
                    if (this._tooltip) {
                        this._tooltip.remove();
                        this._tooltip = null;
                    }
                });
            });
            """;
    }
    
    private static String getHtmlColorBySize(long size) {
        if (size > 10 * MB) return "#dc3545"; // Red
        if (size > 5 * MB) return "#ffc107"; // Yellow
        if (size > 1 * MB) return "#17a2b8"; // Cyan
        return "#667eea"; // Blue
    }
    
    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}