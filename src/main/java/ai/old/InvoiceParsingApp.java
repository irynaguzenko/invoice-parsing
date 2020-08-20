package ai.invoiceparsing;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.storage.GoogleStorageLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class InvoiceParsingApp implements CommandLineRunner {
    private static final String BUCKET_NAME = "cargomatrix-document-ai-2";
    private static final String DESTINATION_FOLDER = "invoice_ocr/";
    private static final String PDF_TYPE = "application/pdf";
    private static Feature DOCUMENT_OCR_FEATURE;

    static {
        DOCUMENT_OCR_FEATURE = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
    }

    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;
    @Autowired
    private Storage storage;

    public static void main(String[] args) {
        SpringApplication.run(InvoiceParsingApp.class, args);
    }

    public void run(String... args) throws Exception {
        parsePdfs();
    }

    private void parsePdfs() throws ExecutionException, InterruptedException {
        List<AsyncAnnotateFileRequest> ocrDocumentRequests = new ArrayList<>();
        storage.list(BUCKET_NAME, Storage.BlobListOption.prefix("invoice/"))
                .iterateAll()
                .forEach(pdf -> {
                    String pdfName = pdf.getName();
                    if (pdfName.endsWith(".pdf"))
                        ocrDocumentRequests.add(ocrDocumentRequest(pdfName));
                });

        OperationFuture<AsyncBatchAnnotateFilesResponse, OperationMetadata> result = this.imageAnnotatorClient.asyncBatchAnnotateFilesAsync(ocrDocumentRequests);
        List<AsyncAnnotateFileResponse> responsesList = result.get().getResponsesList();
        AsyncAnnotateFileResponse asyncAnnotateFileResponse = responsesList.get(0);
        imageAnnotatorClient.close();
    }

    private AsyncAnnotateFileRequest ocrDocumentRequest(String documentName) {
        GoogleStorageLocation document = GoogleStorageLocation.forFile(BUCKET_NAME, documentName);
        GoogleStorageLocation outputLocationPrefix = GoogleStorageLocation.forFile(BUCKET_NAME, DESTINATION_FOLDER + documentName.split("(invoice/)|(.pdf)")[1]);

        GcsSource gcsSource = GcsSource.newBuilder().setUri(document.uriString()).build();
        InputConfig inputConfig = InputConfig.newBuilder().setMimeType(PDF_TYPE).setGcsSource(gcsSource).build();
        GcsDestination gcsDestination = GcsDestination.newBuilder().setUri(outputLocationPrefix.uriString()).build();
        OutputConfig outputConfig = OutputConfig.newBuilder().setGcsDestination(gcsDestination).build();

        return AsyncAnnotateFileRequest.newBuilder().addFeatures(DOCUMENT_OCR_FEATURE).setInputConfig(inputConfig).setOutputConfig(outputConfig).build();
    }
}
