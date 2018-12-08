import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipDecoderUtil {

    public static void main(String[] args) {
        //launch(args);
        //System.out.println(encode("<?xml version=\"1"));

        String hexString =
                "00000000  3c 3f 78 6d 6c 20 76 65 72 73 69 6f 6e 3d 22 31   <?xml version=\"1\n" +
                        "00000010  2e 30 22 20 65 6e 63 6f 64 69 6e 67 3d 22 55 54   .0\" encoding=\"UT\n" +
                        "00000020  46 2d 38 22 3f 3e 20 0a 3c 4f 70 65 6e 53 65 61   F-8\"?>  <OpenSea\n" +
                        "00000030  72 63 68 44 65 73 63 72 69 70 74 69 6f 6e 20 78   rchDescription x\n" +
                        "00000040  6d 6c 6e 73 3d 22 68 74 74 70 3a 2f 2f 61 39 2e   mlns=\"http://a9.\n" +
                        "00000050  63 6f 6d 2f 2d 2f 73 70 65 63 2f 6f 70 65 6e 73   com/-/spec/opens\n" +
                        "00000060  65 61 72 63 68 2f 31 2e 31 2f 22 3e 0a 20 20 20   earch/1.1/\">    \n" +
                        "00000070  20 3c 53 68 6f 72 74 4e 61 6d 65 3e e7 99 be e5    <ShortName>    \n" +
                        "00000080  ba a6 e6 90 9c e7 b4 a2 3c 2f 53 68 6f 72 74 4e           </ShortN\n" +
                        "00000090  61 6d 65 3e 0a 20 20 20 20 3c 55 72 6c 20 74 79   ame>     <Url ty\n" +
                        "000000a0  70 65 3d 22 74 65 78 74 2f 68 74 6d 6c 22 20 74   pe=\"text/html\" t\n" +
                        "000000b0  65 6d 70 6c 61 74 65 3d 22 68 74 74 70 73 3a 2f   emplate=\"https:/\n" +
                        "000000c0  2f 77 77 77 2e 62 61 69 64 75 2e 63 6f 6d 2f 73   /www.baidu.com/s\n" +
                        "000000d0  3f 77 64 3d 7b 73 65 61 72 63 68 54 65 72 6d 73   ?wd={searchTerms\n" +
                        "000000e0  7d 22 2f 3e 20 0a 3c 2f 4f 70 65 6e 53 65 61 72   }\"/>  </OpenSear\n" +
                        "000000f0  63 68 44 65 73 63 72 69 70 74 69 6f 6e 3e 0a      chDescription>  \n";

        Pattern p = Pattern.compile("  ([0-9a-f]{2} ){1,16}  ");
        Matcher m = p.matcher(hexString);
        while (m.find()) {
            System.out.println(m.group());
        }


        System.out.println(decodeHexString(hexString));

        try {
            byte[] byteArrayOutputStream = gzipEncode(hexString);
            System.out.println("gzip encode------------------------");
            System.out.println(new String(byteArrayOutputStream));

            byte[] byteString = gzipDecode(byteArrayOutputStream);
            System.out.println("gzip decode------------------------");
            System.out.println(new String(byteString));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] gzipEncode(String hexString) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(decodeHexByteArray(hexString));
        gzipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] gzipDecode(byte[] byteArrayOutputStream) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byte[] buff = new byte[8 * 1048];
        int readCount;
        while ((readCount = gzipInputStream.read(buff)) != -1) {
            byteArray.write(buff, 0, readCount);
        }

        gzipInputStream.close();
        return byteArray.toByteArray();
    }

    public static String encode(String hexString) {
        StringBuilder result = new StringBuilder();
        byte[] byteText = hexString.getBytes();
        for (int i = 0; i < byteText.length; i += 2) {
            result.append(Integer.toHexString(byteText[i]));
            if (i + 1 <= byteText.length) {
                result.append(" ");
                result.append(Integer.toHexString(byteText[i + 1]));
                result.append(" ");
            }
        }

        return result.toString();
    }

    public static String decodeHexString(String hexString) {
        byte[] bytes = decodeHexByteArray(hexString);
        return new String(bytes);
    }

    public static byte[] decodeHexByteArray(String hexString) {
        /*List<String> hexArray = new ArrayList<>();
        String[] hexStringArray = hexString.split("[0-9a-f]{8}");
        for (String array : hexStringArray) {
            array = array.trim();
            if (array.length() > 0) {
                array = array.substring(0, array.indexOf("  ")).trim();
                hexArray.addAll(Arrays.asList(array.split(" ")));
            }
        }

        byte[] bytes = new byte[hexArray.length];
        int i = 0;
        for (String hex : hexArray) {
            int val = Integer.parseInt(hex, 16);
            bytes[i++] = (byte) (val & 0xff);
        }*/

        String hexArray = hexString.replaceAll("[0-9a-f]{8}  |  .+|[ |\n]", "");//.split("[ |\n]");
        byte[] bytes = new byte[hexArray.length() / 2 + hexArray.length() % 2];
        int bIndex = 0;
        for (int i = 0; i < hexArray.length(); i += 2) {
            String hex = hexArray.charAt(i) + "";
            if (i + 1 < hexArray.length()) {
                hex += hexArray.charAt(i + 1);
            }
            int val = Integer.parseInt(hex.length() == 2 ? hex : "0" + hex, 16);
            bytes[bIndex++] = (byte) (val & 0xff);
        }

        return bytes;
    }

    public static String stringToJSON(String strJson) {
        /*if (!(strJson.matches("[\\w]+[\\\\.|[\\w]][\\w]+\\("))) {
            return strJson;
        }*/

        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else {
                jsonFormat.append(c);
            }
        }
        return jsonFormat.toString();
    }

    // 是空格还是tab
    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append("    ");
        }
        return sbTab.toString();
    }
}
