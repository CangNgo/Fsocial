package com.fsocial.messageservice.Controller;
import com.fsocial.messageservice.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private ChatService chatService;

    /**
     * Endpoint upload file đính kèm.
     * Client gửi multipart/form-data với key "files".
     * Trả về danh sách id của file đã lưu.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        var attachmentIds = chatService.storeFiles(files);
        return ResponseEntity.ok(attachmentIds);
    }

    /**
     * Endpoint tải file đính kèm theo id.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        GridFsResource resource = (GridFsResource) chatService.getFile(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
