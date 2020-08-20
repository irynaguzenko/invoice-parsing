package ai.invoiceparsing;

import ai.invoiceparsing.model.Annotation;
import ai.invoiceparsing.model.Document;
import ai.invoiceparsing.model.TextExtraction;
import ai.invoiceparsing.model.TextSnippet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.documentai.v1beta2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class InvoiceParsingApp implements CommandLineRunner {
    private static final String BUCKET_NAME = "cargomatrix-document-ai-2";
    private static final String DESTINATION_FOLDER = "invoice_ocr/";
    private static final String PROJECT_ID = "cargomatrix-document-ai";
    private static final String LOCATION = "us";
    private static final String INPUT_GS_URI = "gs://cargomatrix-document-ai-2/invoice/1z090a9r0363163258.pdf";
    private DocumentUnderstandingServiceSettings serviceSettings;
    private ObjectMapper objectMapper;

    @Autowired
    private CredentialsProvider credentialsProvider;

    public static void main(String[] args) {
        SpringApplication.run(InvoiceParsingApp.class, args);
    }

    public void run(String... args) throws Exception {
        serviceSettings = DocumentUnderstandingServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        objectMapper = new ObjectMapper();
        quickStart();
    }

    public void quickStart() throws IOException {
        try (DocumentUnderstandingServiceClient client = DocumentUnderstandingServiceClient.create(serviceSettings)) {
            String parent = String.format("projects/%s/locations/%s", PROJECT_ID, LOCATION);
            GcsSource uri = GcsSource.newBuilder().setUri(INPUT_GS_URI).build();
            InputConfig config = InputConfig.newBuilder().setGcsSource(uri).setMimeType("application/pdf").build();
            ProcessDocumentRequest request = ProcessDocumentRequest.newBuilder().setParent(parent).setInputConfig(config).build();
            com.google.cloud.documentai.v1beta2.Document aiDocument = client.processDocument(request);


            File file = new File("labels.jsonl");
//            FileOutputStream outputStream = new FileOutputStream(file);
//            TextExtractionAnnotation textExtraction = TextExtractionAnnotation.newBuilder()
//                    .setTextSegment(TextSegment.newBuilder()
//                            .setStartOffset(0)
//                            .setEndOffset(16)
//                            .build())
//                    .build();
//
//            AnnotationPayload annotation = AnnotationPayload.newBuilder().setTextExtraction(textExtraction).setDisplayName("FirstLabel").build();
//            List<AnnotationPayload> annotations = Collections.singletonList(annotation);
//            TextSnippet textSnippet = TextSnippet.newBuilder()
//                    .setContent(aiDocument.getText())
//                    .build();
//            outputStream.write(JsonFormat.printer().print(annotation).getBytes());

            objectMapper.writeValue(file, new Document(new TextSnippet(aiDocument.getText()), annotations(aiDocument)));
        }
    }

    private List<Annotation> annotations(com.google.cloud.documentai.v1beta2.Document aiDocument) {
        Annotation annotation = new Annotation("disp", new TextExtraction(0, 16));
        return Collections.singletonList(annotation);
    }
}
