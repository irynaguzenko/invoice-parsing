package ai.invoiceparsing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.vision.DocumentOcrTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class VisionLabellingApp implements CommandLineRunner {
    private static final String BUCKET_NAME = "cargomatrix-document-ai-2";
    private static final String DESTINATION_FOLDER = "invoice_ocr/";
    private static final String LABELS_FILE = "labels.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DocumentOcrTemplate documentOcrTemplate;
    @Autowired
    private Storage storage;

    public static void main(String[] args) {
        SpringApplication.run(VisionLabellingApp.class, args);
    }

    public void run(String... args) throws IOException {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        labelJsons();
    }

    private void labelJsons() throws IOException {
        Map<String, String> parsedOrders = parseOrders();
        Map<String, Label> labels = labels();

        parsedOrders.entrySet().stream()
                .forEach(order -> {
                    String orderId = order.getKey();
                    Label label = labels.get(orderId);
                    if (label != null) {

                    }
                });
    }

    private Map<String, String> parseOrders() {
        return StreamSupport.stream(storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(DESTINATION_FOLDER)).iterateAll().spliterator(), false)
                .filter(pdf -> pdf.getName().endsWith(".json"))
                .map(pdf -> {
                    AnnotateFileResponse.Builder annotateFileResponseBuilder = AnnotateFileResponse.newBuilder();
                    String jsonContent = new String(pdf.getContent());
                    try {
                        JsonFormat.parser().ignoringUnknownFields().merge(jsonContent, annotateFileResponseBuilder);
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                    TextAnnotation textAnnotation = annotateFileResponseBuilder.build().getResponses(0).getFullTextAnnotation();
                    String text = textAnnotation.getText();
//                    textAnnotation.getPagesList()
                    return Pair.of(pdf.getName().split("(invoice_ocr/)|(output)")[1],
                            text);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Map<String, Label> labels() throws IOException {
        byte[] labels = storage.get(BlobId.of(BUCKET_NAME, LABELS_FILE)).getContent();
        return objectMapper.readValue(labels, new TypeReference<>() {
        });
    }
}
