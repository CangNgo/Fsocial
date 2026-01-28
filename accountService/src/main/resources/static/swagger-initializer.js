window.onload = function() {
    const ui = SwaggerUIBundle({
        url: "/v3/api-docs",
        dom_id: "#swagger-ui",
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIBundle.SwaggerUIStandalonePreset
        ],
        layout: "BaseLayout",

        // Restore token when reload
        requestInterceptor: (req) => {
            const savedToken = localStorage.getItem("swagger_token");
            if (savedToken) {
                req.headers["Authorization"] = savedToken;
            }
            return req;
        },

        // Save token when user clicks Authorize
        responseInterceptor: (res) => {
            const authHeader = res.headers.authorization;
            if (authHeader) {
                localStorage.setItem("swagger_token", authHeader);
            }
            return res;
        },
    });

    // Load UI
    window.ui = ui;
};
