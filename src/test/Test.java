package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import com.opencsv.CSVReader;

public class Test {
    public Test() {
    }

    public static void main(String args[]) {
        try {
            functionCall();
            //csvJson();
            //kick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 引数の文字列(Shift_JIS)を、UTF-8にエンコードする。
     *
     * @param value 変換対象の文字列
     * @return エンコードされた文字列
     */

    public static String sjisToUtf8(String value) throws UnsupportedEncodingException {
        byte[] srcStream = value.getBytes("SJIS");
        byte[] destStream = (new String(srcStream, "SJIS")).getBytes("UTF-8");
        value = new String(destStream, "UTF-8");
        value = Test.convert(value, "SJIS", "UTF-8");
        return value;
    }

    /**
     * 引数の文字列(UTF-8)を、Shift_JISにエンコードする。
     *
     * @param value 変換対象の文字列
     * @return エンコードされた文字列
     */
    public static String utf8ToSjis(String value) throws UnsupportedEncodingException {
        byte[] srcStream = value.getBytes("UTF-8");
        value = convert(new String(srcStream, "UTF-8"), "UTF-8", "SJIS");
        byte[] destStream = value.getBytes("SJIS");
        value = new String(destStream, "SJIS");
        return value;
    }

    /**
     * 引数の文字列を、エンコードする。
     *
     * @param value 変換対象の文字列
     * @param src 変換前の文字コード
     * @param dest 変換後の文字コード
     * @return エンコードされた文字列
     */
    private static String convert(String value, String src, String dest) throws UnsupportedEncodingException {
        Map<String, String> conversion = createConversionMap(src, dest);
        char oldChar;
        char newChar;
        String key;
        for (Iterator<String> itr = conversion.keySet().iterator(); itr.hasNext();) {
            key = itr.next();
            oldChar = toChar(key);
            newChar = toChar(conversion.get(key));
            value = value.replace(oldChar, newChar);
        }
        return value;
    }

    /**
     * エンコード情報を作成する
     *
     * @param src 変換前の文字コード
     * @param dest 変換後の文字コード
     * @return エンコードされた文字列
     */
    private static Map<String, String> createConversionMap(String src, String dest)
            throws UnsupportedEncodingException {
        Map<String, String> conversion = new HashMap<String, String>();
        if ((src.equals("UTF-8")) && (dest.equals("SJIS"))) {
            // －（全角マイナス）
            conversion.put("U+FF0D", "U+2212");
            // ～（全角チルダ）
            conversion.put("U+FF5E", "U+301C");
            // ￠（セント）
            conversion.put("U+FFE0", "U+00A2");
            // ￡（ポンド）
            conversion.put("U+FFE1", "U+00A3");
            // ￢（ノット）
            conversion.put("U+FFE2", "U+00AC");
            // ―（全角マイナスより少し幅のある文字）
            conversion.put("U+2015", "U+2014");
            // ∥（半角パイプが2つ並んだような文字）
            conversion.put("U+2225", "U+2016");

        } else if ((src.equals("SJIS")) && (dest.equals("UTF-8"))) {
            // －（全角マイナス）
            conversion.put("U+2212", "U+FF0D");
            // ～（全角チルダ）
            conversion.put("U+301C", "U+FF5E");
            // ￠（セント）
            conversion.put("U+00A2", "U+FFE0");
            // ￡（ポンド）
            conversion.put("U+00A3", "U+FFE1");
            // ￢（ノット）
            conversion.put("U+00AC", "U+FFE2");
            // ―（全角マイナスより少し幅のある文字）
            conversion.put("U+2014", "U+2015");
            // ∥（半角パイプが2つ並んだような文字）
            conversion.put("U+2016", "U+2225");

        } else {
            throw new UnsupportedEncodingException("この文字コードはサポートしていません。\n・src=" + src + ",dest=" + dest);
        }
        return conversion;
    }

    /**
     * 16進表記の文字を取得する。
     *
     * @param value 変換対象の文字列
     * @return 16進表記の文字
     */
    private static char toChar(String value) {
        return (char) Integer.parseInt(value.trim().substring("U+".length()), 16);
    }

    public static void csvJson() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("C:\\src\\test.csv"));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + " " + nextLine[1]);
        }
    }

    public static String functionCall() throws IOException {
        StringBuilder builder = new StringBuilder();

        String path = new File("src\\js\\commonFunction.js").toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String string = reader.readLine();
            while (string != null) {
                builder.append(string + System.getProperty("line.separator"));
                string = reader.readLine();
            }
        }
        String utf8Str = new String(builder.toString().replaceAll("    ", "").replaceAll("\r\n", " "));
        //String bStr =  builder.toString().replaceAll("    ", "").replaceAll("\r\n", " ").getBytes( "UTF-8");
        //System.out.println("rtn: " + builder.toString());
        System.out.println("rtn: " + utf8Str);
        return utf8Str;
    }

    private static final String JS_FUNCTION_NAME = "isEmpty";

    public static String doParse(String Str) throws IOException {

        Reader envjsReader = null;
        Reader jqueryReader = null;
        Reader scriptReader = null;
        try {
            envjsReader = new FileReader(new File("src\\env.rhino.1.2.js"));
            jqueryReader = new FileReader(new File("src\\jquery-1.3.2.min.js"));
            scriptReader = new FileReader(new File("src\\js\\isEmpty.js"));
        } catch (FileNotFoundException e) {
            //サンプルなのでエラー処理は考慮せず
            e.printStackTrace();
        }

        //env.jsはshellでしか定義されていないfunction(print等)を必要とするため、Global Contextを取得
        Global global = new Global();
        Context cx = ContextFactory.getGlobal().enterContext();
        global.init(cx);
        cx.setOptimizationLevel(-1);
        cx.setLanguageVersion(Context.VERSION_1_5);

        Scriptable scope = cx.initStandardObjects(global);
        try {

            //env.jsのコンパイル
            Script envjs = cx.compileReader(envjsReader,
                    "env.rhino.1.2.js",
                    1, null);
            envjsReader.close();
            //読みこみ。
            //execすることで複数のjsファイルをContextに読みこみ可能
            envjs.exec(cx, scope);

            //jqueryのコンパイルと読みこみ
            Script jquery = cx.compileReader(jqueryReader,
                    "jquery-1.3.2.min.js",
                    1, null);
            jqueryReader.close();
            jquery.exec(cx, scope);

            //スクリプトのコンパイルと読みこみ
            Script script = cx.compileReader(scriptReader,
                    "isEmpty.js",
                    1, null);
            scriptReader.close();
            script.exec(cx, scope);

            //JavaScript function doParse() 実行と結果の取得
            Object result = cx.evaluateString(scope, JS_FUNCTION_NAME + "(" + Str + ")", "<cmd>", 1, null);

            //System.out.println("rtn: " + Context.toString(result));
            return Context.toString(result);
        } catch (IOException e) {
            // サンプルなので例外処理は考慮せず
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static void kick() {

        try {
            //ランタイム取得
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("notepad.exe"); //パスが通っていればファイル名だけでOK
            //System.out.print();
            //exeを実行する

            //runtime.exec("C:\\hoge.exe");  //フルパスでの指定もOK

        } catch (IOException e) {
            //指定したファイルが無い場合の処理
            e.printStackTrace();
        }
    }

}
