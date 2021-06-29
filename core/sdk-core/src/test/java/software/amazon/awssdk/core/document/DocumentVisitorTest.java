package software.amazon.awssdk.core.document;

import org.testng.annotations.Test;
import software.amazon.awssdk.core.SdkNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


public class DocumentVisitorTest {

    @Test
    public void testACustomDocumentVisitor() {

        // Constructing a Document for CustomClass
        final Document build = Document.mapBuilder()
                .putDocument("customClassFromMap",
                        Document.mapBuilder().putString("innerStringField", "innerValue")
                                .putNumber("innerIntField", SdkNumber.fromLong(99)).build())
                .putString("outerStringField", "outerValue")
                .putNumber("outerLongField", SdkNumber.fromDouble(1)).build();
        final CustomClass customClassExtracted = build.accept(new CustomDocumentVisitor());
        // Expected  CustomClass
        final CustomClassFromMap innerMap = new CustomClassFromMap();
        innerMap.setInnerIntField(99);
        innerMap.setInnerStringField("innerValue");
        CustomClass expectedCustomClass = new CustomClass();
        expectedCustomClass.setOuterLongField(1L);
        expectedCustomClass.setOuterStringField("outerValue");
        expectedCustomClass.setCustomClassFromMap(innerMap);
        assertThat(customClassExtracted).isEqualTo(expectedCustomClass);

        System.out.println(build.toString());
    }

    @Test
    public void testDocumentVisitorWhenMethodNotImplemented(){
        DocumentVisitor<Object> documentVisitor = new DocumentVisitor<Object>() {
            @Override
            public Object visitNull() { return null; }
            @Override
            public Object visitBoolean(Boolean document) { return null; }
            @Override
            public Object visitString(String document) { return null; }
            @Override
            public Object visitNumber(SdkNumber document) { return null; }
            @Override
            public Object visitMap(Map<String, Document> documentMap) { return null; }
            @Override
            public Object visitList(List<Document> documentList) { return null; }
        };
        assertThat(Document.fromNumber(2).accept(documentVisitor)).isEqualTo(null);
        assertThat(Document.fromString("2").accept(documentVisitor)).isEqualTo(null);
        assertThat(Document.fromNull().accept(documentVisitor)).isEqualTo(null);
        assertThat(Document.fromBoolean(true).accept(documentVisitor)).isEqualTo(null);
        assertThat(Document.fromMap(new LinkedHashMap<>()).accept(documentVisitor)).isEqualTo(null);
        assertThat(Document.fromList(new ArrayList<>()).accept(documentVisitor)).isEqualTo(null);
    }

    /*
    Below are auxiliary classes to test Custom class visitors.
     */
    private static class CustomClassFromMap {
        String innerStringField;
        Integer innerIntField;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CustomClassFromMap)) return false;
            CustomClassFromMap that = (CustomClassFromMap) o;
            return Objects.equals(innerStringField, that.innerStringField) &&
                    Objects.equals(innerIntField, that.innerIntField);
        }
        @Override
        public int hashCode() {
            return Objects.hash(innerStringField, innerIntField);
        }

        public void setInnerStringField(String innerStringField) {
            this.innerStringField = innerStringField;
        }
        public void setInnerIntField(Integer innerIntField) {
            this.innerIntField = innerIntField;
        }
    }

    private static class CustomClass implements Serializable {
        String outerStringField;
        Long outerLongField;
        CustomClassFromMap customClassFromMap;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CustomClass)) return false;
            CustomClass that = (CustomClass) o;
            return Objects.equals(outerStringField, that.outerStringField) &&
                    Objects.equals(outerLongField, that.outerLongField) &&
                    Objects.equals(customClassFromMap, that.customClassFromMap);
        }

        public void setOuterStringField(String outerStringField) {
            this.outerStringField = outerStringField;
        }

        public void setOuterLongField(Long outerLongField) {
            this.outerLongField = outerLongField;
        }

        public void setCustomClassFromMap(CustomClassFromMap customClassFromMap) {
            this.customClassFromMap = customClassFromMap;
        }
    }

    /**
     * Visitor to fetch attribute values for CustomClassFromMap.
     */
    private static class CustomMapDocumentVisitor implements DocumentVisitor<CustomClassFromMap> {
        @Override
        public CustomClassFromMap visitNull() { return null; }
        @Override
        public CustomClassFromMap visitBoolean(Boolean document) { return null; }
        @Override
        public CustomClassFromMap visitString(String document) { return null; }
        @Override
        public CustomClassFromMap visitNumber(SdkNumber document) { return null; }

        @Override
        public CustomClassFromMap visitMap(Map<String, Document> documentMap) {
            CustomClassFromMap customClassFromMap = new CustomClassFromMap();
            documentMap.entrySet().stream().forEach(stringDocumentEntry -> {
                if ("innerStringField".equals(stringDocumentEntry.getKey())) {
                    customClassFromMap.setInnerStringField(stringDocumentEntry.getValue().accept(new StringDocumentVisitor()));
                } else if ("innerIntField".equals(stringDocumentEntry.getKey())) {
                    customClassFromMap.setInnerIntField(stringDocumentEntry.getValue().accept(new NumberDocumentVisitor()).intValue());

                }
            });
            return customClassFromMap;
        }

        @Override
        public CustomClassFromMap visitList(List<Document> documentList) {
            return null;
        }
    }

    /**
     * Visitor to fetch attribute values for CustomClass.
     */
    private static class CustomDocumentVisitor implements DocumentVisitor<CustomClass> {
        private final CustomClass customClass = new CustomClass();

        @Override
        public CustomClass visitNull() {
            return null;
        }

        @Override
        public CustomClass visitBoolean(Boolean document) {
            return null;
        }

        @Override
        public CustomClass visitString(String document) {
            return null;
        }

        @Override
        public CustomClass visitNumber(SdkNumber document) {
            return null;
        }

        @Override
        public CustomClass visitMap(Map<String, Document> documentMap) {
            documentMap.entrySet().stream().forEach(stringDocumentEntry -> {
                if ("customClassFromMap".equals(stringDocumentEntry.getKey())) {
                    final CustomMapDocumentVisitor customMapDocumentVisitor = new CustomMapDocumentVisitor();
                    customClass.setCustomClassFromMap(stringDocumentEntry.getValue().accept(customMapDocumentVisitor));
                } else if ("outerStringField".equals(stringDocumentEntry.getKey())) {
                    customClass.setOuterStringField(
                            stringDocumentEntry.getValue().accept(new StringDocumentVisitor()));
                } else if ("outerLongField".equals(stringDocumentEntry.getKey())) {
                    customClass.setOuterLongField(stringDocumentEntry.getValue().accept(new NumberDocumentVisitor()).longValue());
                }
            });
            return customClass;
        }

        @Override
        public CustomClass visitList(List<Document> documentList) {
            return null;
        }
    }

    private static class StringDocumentVisitor implements DocumentVisitor<String> {
        @Override
        public String visitNull() { return null; }
        @Override
        public String visitBoolean(Boolean document) { return null; }
        @Override
        public String visitString(String document) { return document; }
        @Override
        public String visitNumber(SdkNumber document) { return null; }
        @Override
        public String visitMap(Map<String, Document> documentMap) { return null; }
        @Override
        public String visitList(List<Document> documentList) { return null; }
    }

    private static class NumberDocumentVisitor implements DocumentVisitor<SdkNumber> {
        @Override
        public SdkNumber visitNull() { return null; }
        @Override
        public SdkNumber visitBoolean(Boolean document) { return null; }
        @Override
        public SdkNumber visitString(String document) { return null; }
        @Override
        public SdkNumber visitNumber(SdkNumber document) { return document; }
        @Override
        public SdkNumber visitMap(Map<String, Document> documentMap) { return null; }
        @Override
        public SdkNumber visitList(List<Document> documentList) { return null; }
    }
}
