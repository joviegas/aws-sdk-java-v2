package software.amazon.awssdk.core.document;

import org.testng.annotations.Test;
import software.amazon.awssdk.core.SdkNumber;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UnwrapDocumentTest {

    @Test
    public void testMapDocumentUnwrap() {
        final Document mapDocument = Document.mapBuilder().putString("key", "stringValue")
                .putDocument("documentKey", Document.fromString("documentValue"))
                .build();
        final Object unwrappedMapObject = mapDocument.unwrap();
        final long unwrappedMapCountAsString = ((Map<?, ?>) unwrappedMapObject).entrySet().stream()
                .filter(k -> k.getKey() instanceof String && k.getValue() instanceof String).count();
        final long unwrappedMapCountAsDocument = ((Map<?, ?>) unwrappedMapObject).entrySet().stream()
                .filter(k -> k.getKey() instanceof String && k.getValue() instanceof Document).count();
        assertThat(unwrappedMapCountAsString).isEqualTo(2);
        assertThat(unwrappedMapCountAsDocument).isEqualTo(0);
    }

    @Test
    public void testListDocumentUnwrap() {
        final Document documentList = Document.fromList(Arrays.asList(Document.fromNumber(SdkNumber.fromLong(1)), Document.fromNumber(SdkNumber.fromLong(2))));
        final Object documentListAsObjects = documentList.unwrap();
        final Optional strippedAsSDKNumber = ((List) documentListAsObjects)
                .stream().filter(e -> e instanceof String).findAny();
        final Optional strippedAsDocuments = ((List) documentListAsObjects)
                .stream().filter(e -> e instanceof Document).findAny();
        assertThat(strippedAsSDKNumber.isPresent()).isTrue();
        assertThat(strippedAsDocuments.isPresent()).isFalse();
    }

    @Test
    public void testStringDocumentUnwrapt() {
        final Document testDocument = Document.fromString("testDocument");
        assertThat(testDocument.unwrap()).isEqualTo("testDocument");
    }

    @Test
    public void testNumberDocumentUnwrap() {
        final Document testDocument = Document.fromNumber(SdkNumber.fromLong(2));
        assertThat(testDocument.unwrap()).isEqualTo(SdkNumber.fromLong(2).stringValue());
    }

    @Test
    public void testBoolanDocumentUnwrap() {
        final Document testDocument = Document.fromBoolean(true);
        assertThat(testDocument.unwrap()).isEqualTo(true);
    }

    @Test
    public void testNullDocumentUnwrap() {
        final Document testDocument = Document.fromNull();
        assertThat(testDocument.unwrap()).isNull();
    }


}
