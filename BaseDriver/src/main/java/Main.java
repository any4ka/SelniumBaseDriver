import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static String folder = System.getProperty("user.dir") + "//src//test//java//com//creditcards//";

    /**
     * <p>
     * gen:suite <i>suite name</i> - generate suite
     * gen:pageObj <i>suite name</i> <i>page name</i> <i>mobile or web</i>- generate a page object
     * gen:test <i>suite name</i> <i>test name</i> <i>driver</i> - generate a test - driver can be web or mobile
     * help - list of help document
     * gen:generalHelper <i>helper name</i> - generate a general helper for all tests to use
     * </p>
     * stringBuilder.append("@param args
     */
    public static void main(String[] args) throws Exception {

        String stringBuilder = "gen:suite <suite name> - generate suite\n" +
                "gen:pageObj <suite name> <page name> <mobile or web> - generate a page object\n" +
                "gen:test <suite name> <test name> <driver> - generate a test - driver can be web or mobile\n" +
                "help - list of help document\n" +
                "gen:generalHelper <helper name> - generate a general helper for all tests to use\n";
        switch (args[0]) {
            case "help":
                System.out.println(stringBuilder);
                break;
            case "gen:suite":
                generateSuite(lowerCaseFirstLetter(args[1]));
                break;
            case "gen:pageObj":
                generatePageObject(lowerCaseFirstLetter(args[1]), capitalizeFirstLetter(args[2]), args[3]);
                break;
            case "gen:test":
                generateTest(lowerCaseFirstLetter(args[1]), capitalizeFirstLetter(args[2]), args[3]);
                break;
            default:
                System.out.println(stringBuilder);
        }

    }

    private static void generateTest(String suiteName, String className, String driver) throws Exception {
        if (!(new File(folder).exists())) {
            generateSuite(suiteName);
        }
        File file = new File(folder + "//" + suiteName + "//" + className + ".java");
        Map<String, String> values = new HashMap<>();
        values.put("suite", suiteName);
        values.put("class", className);
        if (!file.exists()) {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            if (driver.equals("mobile")) {
                String text = GeneratedText.TEST_MOBILE.text().replace("${suite}", values.get("suite"));
                text = text.replace("${class}", values.get("class"));
                writer.println(text);
            } else {
                String text = GeneratedText.TEST_WEB.text().replace("${suite}", values.get("suite"));
                text = text.replace("${class}", values.get("class"));
                writer.println(text);
            }
            writer.close();
        } else {
            System.out.println(file.getName() + " already exists");
        }
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static String lowerCaseFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toLowerCase() + original.substring(1);
    }

    public static void generateSuite(String suite) {
        StringBuilder builder = new StringBuilder();
        builder.append(folder).append(suite);
        new File(builder.toString()).mkdir();
        System.out.println(builder.append(" has been created").toString());
        StringBuilder resourceFolder = new StringBuilder();
        resourceFolder.append(System.getProperty("user.dir")).append("//src//test//resources//testNG//").append(capitalizeFirstLetter(suite));
        new File(resourceFolder.toString()).mkdir();
        System.out.println(resourceFolder.append(" has been created").toString());
    }

    public static void generatePageObject(String suiteName, String className, String driver) throws Exception {
        String folderPage = folder + "//" + suiteName + "//" +
                ((driver.equals("web")) ? "//pages//" : "//screens//");
        if (!(new File(folderPage).exists())) {
            new File(folderPage).mkdir();
        }
        File file = new File(folderPage + className + ".java");
        Map<String, String> values = new HashMap<>();
        values.put("suite", suiteName);
        values.put("class", className);
        if (!file.exists()) {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            if (driver.equals("mobile")) {
                String text = GeneratedText.PAGE_MOBILE.text().replace("${suite}", values.get("suite"));
                text = text.replace("${class}", values.get("class"));
                writer.println(text);
            } else {
                String text = GeneratedText.PAGE_WEB.text().replace("${suite}", values.get("suite"));
                text = text.replace("${class}", values.get("class"));
                writer.println(text);
            }
            writer.close();
        } else {
            System.out.println(file.getName() + " already exists");
        }
    }
}
