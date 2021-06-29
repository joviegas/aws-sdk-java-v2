package software.amazon.awssdk.core.document;

import org.junit.Test;
import software.amazon.awssdk.core.SdkNumber;
import software.amazon.awssdk.core.document.internal.ListDocument;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DocumentTypeTest {

    final static String TEST_STRING_VALUE = "testString";
    final static Long TEST_LONG_VALUE = 99L;


    @Test
    public void testNullDocument() {

        Document nullDocument = Document.fromNull();

        assertThat(nullDocument.isNull()).isTrue();
        assertThat(nullDocument.isString()).isFalse();
        assertThat(nullDocument.isBoolean()).isFalse();
        assertThat(nullDocument.isNumber()).isFalse();
        assertThat(nullDocument.isMap()).isFalse();
        assertThat(nullDocument.isList()).isFalse();

        assertThatThrownBy(() -> nullDocument.asBoolean()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> nullDocument.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> nullDocument.asMap()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> nullDocument.asNumber()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> nullDocument.asMap()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void stringDocumentTest() {
        final Document testDocumentString = Document.fromString(TEST_STRING_VALUE);
        assertThat(testDocumentString.asString()).isEqualTo(TEST_STRING_VALUE);
        assertThat(testDocumentString.isString()).isTrue();
        assertThat(testDocumentString.isBoolean()).isFalse();
        assertThat(testDocumentString.isNumber()).isFalse();
        assertThat(testDocumentString.isMap()).isFalse();
        assertThat(testDocumentString.isNull()).isFalse();
        assertThat(testDocumentString.isList()).isFalse();
        assertThatThrownBy(() -> testDocumentString.asBoolean()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentString.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentString.asMap()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentString.asNumber()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void booleanDocumentTest() {
        final Document testDocumentBoolean = Document.fromBoolean(true);

        assertThat(testDocumentBoolean.asBoolean()).isEqualTo(true);
        assertThat(testDocumentBoolean.isString()).isFalse();
        assertThat(testDocumentBoolean.isBoolean()).isTrue();
        assertThat(testDocumentBoolean.isNumber()).isFalse();
        assertThat(testDocumentBoolean.isMap()).isFalse();
        assertThat(testDocumentBoolean.isNull()).isFalse();
        assertThat(testDocumentBoolean.isList()).isFalse();
        assertThatThrownBy(() -> testDocumentBoolean.asString()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentBoolean.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentBoolean.asMap()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> testDocumentBoolean.asNumber()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void numberDocumentTest() {
        final Document documentNumber = Document.fromNumber(SdkNumber.fromLong(TEST_LONG_VALUE));

        assertThat(documentNumber.asNumber().longValue()).isEqualTo(TEST_LONG_VALUE);
        assertThat(documentNumber.asNumber()).isEqualTo(SdkNumber.fromLong(TEST_LONG_VALUE));
        assertThat(documentNumber.isString()).isFalse();
        assertThat(documentNumber.isBoolean()).isFalse();
        assertThat(documentNumber.isNumber()).isTrue();
        assertThat(documentNumber.isMap()).isFalse();
        assertThat(documentNumber.isNull()).isFalse();
        assertThat(documentNumber.isList()).isFalse();
        assertThatThrownBy(() -> documentNumber.asString()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> documentNumber.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> documentNumber.asMap()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> documentNumber.asBoolean()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void numberDocumentFromPrimitive() throws ParseException {

        assertThat(Document.fromNumber("2").asNumber()).isEqualTo(SdkNumber.fromString("2"));
        assertThat(Document.fromNumber(2).asNumber()).isEqualTo(SdkNumber.fromInteger(2));
        assertThat(Document.fromNumber(2L).asNumber()).isEqualTo(SdkNumber.fromLong(2));
        assertThat(Document.fromNumber(2.0).asNumber()).isEqualTo(SdkNumber.fromDouble(2.0));
        assertThat(Document.fromNumber(2.0f).asNumber()).isEqualTo(SdkNumber.fromFloat(2));
        assertThat(Document.fromNumber(2.0f).asNumber()).isEqualTo(SdkNumber.fromFloat(2));
        assertThat(Document.fromNumber(BigDecimal.ONE).asNumber()).isEqualTo(SdkNumber.fromBigDecimal(new BigDecimal(1)));
        assertThat(Document.fromNumber(BigInteger.TEN).asNumber()).isEqualTo(SdkNumber.fromBigInteger(new BigInteger("10")));

        assertThatThrownBy(() -> Document.fromNumber("foo").asNumber().longValue()).isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void mapDocumentFromPrimitiveNumberBuilders()  {

        final Document actualDocumentMap = Document.mapBuilder().putNumber("int", 1)
                .putNumber("long", 2L)
                .putNumber("float", 3.0f)
                .putNumber("double", 4.00)
                .putNumber("string", "5")
                .putNumber("bigDecimal", BigDecimal.TEN)
                .putNumber("bigInteger", BigInteger.TEN).build();


        Map<String, Document> numberMap = new LinkedHashMap<>();
        numberMap.put("int", Document.fromNumber(1));
        numberMap.put("long", Document.fromNumber(2L));
        numberMap.put("float", Document.fromNumber(3.0f));
        numberMap.put("double", Document.fromNumber(4.00));
        numberMap.put("string", Document.fromNumber("5"));
        numberMap.put("bigDecimal",Document.fromNumber(BigDecimal.TEN));
        numberMap.put("bigInteger", Document.fromNumber(BigInteger.TEN));

        final Document expectedMap = Document.fromMap(numberMap);

        assertThat(actualDocumentMap).isEqualTo(expectedMap);
    }

    @Test
    public void listDocumentFromPrimitiveNumberBuilders() {

        final Document actualDocList = ListDocument.listBuilder()
                .addNumber(1)
                .addNumber(2L)
                .addNumber(3.0f)
                .addNumber(4.0)
                .addNumber(BigDecimal.TEN)
                .addNumber(BigInteger.TEN)
                .addNumber("1000")
                .build();

        List<Document> numberList = new ArrayList<>();
        numberList.add(Document.fromNumber(1));
        numberList.add(Document.fromNumber(2l));
        numberList.add(Document.fromNumber(3.0f));
        numberList.add(Document.fromNumber(4.0));
        numberList.add(Document.fromNumber(BigDecimal.TEN));
        numberList.add(Document.fromNumber(BigInteger.TEN));
        numberList.add(Document.fromNumber("1000"));
        assertThat(actualDocList.asList()).isEqualTo(numberList);
    }


    @Test
    public void mapDocumentTestWithMapBuilders() {

        final Document actualDocumentMap = Document.mapBuilder()
                .putString("key", "value")
                .putNull("nullKey")
                .putNumber("numberKey", SdkNumber.fromBigDecimal(new BigDecimal(100.1234567)))
                .putList("listKey", listBuilder -> listBuilder.addNumber(SdkNumber.fromLong(9)))
                .build();

        final LinkedHashMap<String, Object> expectedMapObject = new LinkedHashMap<>();
        expectedMapObject.put("key", "value");
        expectedMapObject.put("nullKey", null);
        expectedMapObject.put("numberKey", new BigDecimal(100.1234567).toString());
        expectedMapObject.put("listKey", Arrays.asList("9"));

        assertThat(actualDocumentMap.isString()).isFalse();
        assertThat(actualDocumentMap.isBoolean()).isFalse();
        assertThat(actualDocumentMap.isNumber()).isFalse();
        assertThat(actualDocumentMap.isMap()).isTrue();
        assertThat(actualDocumentMap.isNull()).isFalse();
        assertThat(actualDocumentMap.isList()).isFalse();
        assertThat(actualDocumentMap.unwrap()).isEqualTo(expectedMapObject);
        assertThatThrownBy(() -> actualDocumentMap.asString()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asNumber()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asBoolean()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void mapDocumentTestFromMapConstructors() {
        final LinkedHashMap<String, Object> expectedMapObject = new LinkedHashMap<>();
        expectedMapObject.put("key", "value");
        expectedMapObject.put("nullKey", null);
        expectedMapObject.put("numberKey", new BigDecimal(100.1234567).toString());
        expectedMapObject.put("listKey", Arrays.asList("9"));

        final LinkedHashMap<String, Document> map = new LinkedHashMap<>();
        map.put("key", Document.fromString("value"));
        map.put("nullKey", Document.fromNull());
        map.put("numberKey", Document.fromNumber(SdkNumber.fromBigDecimal(new BigDecimal(100.1234567))));
        map.put("listKey", Document.fromList(Arrays.asList(Document.fromNumber(SdkNumber.fromLong(9)))));

        Document actualDocumentMap = Document.fromMap(map);

        assertThat(actualDocumentMap.isString()).isFalse();
        assertThat(actualDocumentMap.isBoolean()).isFalse();
        assertThat(actualDocumentMap.isNumber()).isFalse();
        assertThat(actualDocumentMap.isMap()).isTrue();
        assertThat(actualDocumentMap.isNull()).isFalse();
        assertThat(actualDocumentMap.isList()).isFalse();
        assertThat(actualDocumentMap.unwrap()).isEqualTo(expectedMapObject);
        assertThatThrownBy(() -> actualDocumentMap.asString()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asList()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asNumber()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> actualDocumentMap.asBoolean()).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void listDocumentTest() {

        final Document listBuilder = Document.listBuilder()
                .addDocument(Document.fromString("one")).addNumber(SdkNumber.fromLong(2)).addBoolean(true)
                .addMap(mapBuilder -> mapBuilder.putString("consumerKey", "consumerKeyMap"))
                .build();

        assertThat(listBuilder.isString()).isFalse();
        assertThat(listBuilder.isBoolean()).isFalse();
        assertThat(listBuilder.isNumber()).isFalse();
        assertThat(listBuilder.isMap()).isFalse();
        assertThat(listBuilder.isNull()).isFalse();
        assertThat(listBuilder.isList()).isTrue();
        assertThatThrownBy(() -> listBuilder.asString()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> listBuilder.asMap()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> listBuilder.asNumber()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> listBuilder.asBoolean()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(listBuilder.asList().get(0).asString()).isEqualTo("one");
        assertThat(listBuilder.asList().get(1).asNumber()).isEqualTo(SdkNumber.fromLong(2));
        assertThat(listBuilder.asList().get(2).asBoolean()).isTrue();
        assertThat(listBuilder.asList().get(3).isMap()).isTrue();
        assertThat(listBuilder.asList().get(3)).isEqualTo(Document.mapBuilder().putString("consumerKey", "consumerKeyMap")
                .build());
        assertThat(Document.listBuilder().addNumber(SdkNumber.fromLong(1)).addNumber(SdkNumber.fromLong(2)).addNumber(SdkNumber.fromLong(3))
                .addNumber(SdkNumber.fromLong(4)).addNumber(SdkNumber.fromLong(5)).build().asList())
                .isEqualTo(Arrays.asList(Document.fromNumber(SdkNumber.fromLong(1)), Document.fromNumber(SdkNumber.fromLong(2)),
                        Document.fromNumber(SdkNumber.fromLong(3)), Document.fromNumber(SdkNumber.fromLong(4)), Document.fromNumber(SdkNumber.fromLong(5))));
    }

    @Test
    public void testStringDocumentEscapeQuotes() {
        // Actual String is <start>"mid"<end>
        Document docWithQuotes = Document.fromString("<start>" + '\u0022' + "mid" + '\u0022' + "<end>");
        //We expect the quotes <start>\"mid\"<end>
        assertThat(docWithQuotes.toString()).isEqualTo("\"<start>\\\"mid\\\"<end>\"");
        Document docWithNoQuotes = Document.fromString("testString");
        assertThat(docWithNoQuotes.toString()).isEqualTo("\"testString\"");
        assertThat("\"" + docWithNoQuotes.asString() + "\"").isEqualTo(docWithNoQuotes.toString());
    }

    @Test
    public void testStringDocumentEscapeBackSlash() {
        String testString = "test\\String";
        Document document = Document.fromString(testString);
        assertThat(document.toString()).isEqualTo("\"test\\\\String\"");
        assertThat("\"" + document.asString() + "\"").isNotEqualTo(document.toString());


    }
}
