package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ExAPI {
    public static final boolean CONST_IS_DEBUG = false;
    public static final String CONST_STR_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String CONST_STR_DATEFORMAT_MS = "yyyyMMddHHmmss";
    public static final SimpleDateFormat CONST_SIMPLEDATEFORMAT_MS = new SimpleDateFormat(CONST_STR_DATEFORMAT_MS);
    public static final String CONST_STR_SHIFT_JIS = "Shift-JIS";
    
    public ExAPI() {
    }
    
    public static void main(String args[]) {
        //new ExAPI()._saveFile(null, null, null);
        new ExAPI().openIIIDialog(533545, null);
        //new ExAPI().cmdExec("C:\\BarcodePrinter\\BarcodePrinter.exe");
    }
    
    public ArrayList<HashMap<String, String>> openIIIDialog(int cCode, InputStream zipInputStream) {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            Path tmpDir = null;
            try {
                tmpDir = Files.createTempDirectory(null);
                String tmpFile = tmpDir + "\\tmp" + (CONST_SIMPLEDATEFORMAT_MS.format(new Date())) + ".txt";
                //saveFile(zipInputStream, tmpFile.replace(".txt", ".zip"));
                decode(new ZipInputStream(zipInputStream), tmpDir.toString());
                cmdExec(tmpDir + "\\InspectionItemInputAssistantWindow.exe " + tmpFile + " " + cCode);
                //String tmpFile = "C:\\DevApp\\FRI\\ECM\\ConsoleApplication10\\bin\\Debug\\ConsoleApplication10.exe";
                //cmdExec(tmpFile);
                System.out.println(
                        "execcmd > " + tmpDir + "\\InspectionItemInputAssistantWindow.exe " + tmpFile + " " + cCode);
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(tmpFile), CONST_STR_SHIFT_JIS));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    String json = sb.toString();
                    Gson gson = new GsonBuilder().setDateFormat(CONST_STR_DATEFORMAT).create();
                    InspectionItems iItems = gson.fromJson(json, InspectionItems.class);
                    if (iItems != null && iItems.InspectionItems != null && iItems.InspectionItems.size() > 0) {
                        HashMap<String, String> lCls = iItems.getLargeClassification() != null
                                ? new HashMap<String, String>(iItems.getLargeClassification()) : null;
                        HashMap<String, String> mCls = iItems.getMediumClassification() != null
                                ? new HashMap<String, String>(iItems.getMediumClassification()) : null;
                        HashMap<String, String> sCls = iItems.getSmallClassification() != null
                                ? new HashMap<String, String>(iItems.getSmallClassification()) : null;
                        for (int i = 0; i < iItems.InspectionItems.size(); i++) {
                            HashMap<String, String> mHash = new HashMap<String, String>(iItems.InspectionItems.get(i));
                            if (lCls != null) {
                                mHash.putAll(lCls);
                            }
                            if (mCls != null) {
                                mHash.putAll(mCls);
                            }
                            if (sCls != null) {
                                mHash.putAll(sCls);
                            }
                            result.add(mHash);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    throw e;
                } catch (Exception e) {
                    throw e;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    delete(new File(tmpDir.toString()));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /*
    public ArrayList<HashMap<String, String>> openIIIDialog(int cCode, InputStream zipInputStream) {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            Path tmpDir = null;
            try {
                tmpDir = Files.createTempDirectory(null);
                String tmpFile = tmpDir + "\\tmp" + (CONST_SIMPLEDATEFORMAT_MS.format(new Date())) + ".txt";
                //saveFile(zipInputStream, tmpFile.replace(".txt", ".zip"));
                decode(new ZipInputStream(zipInputStream), tmpDir.toString());
                cmdExec(tmpDir + "\\InspectionItemInputAssistantWindow.exe " + tmpFile + " " + cCode);
                System.out.println(
                        "execcmd > " + tmpDir + "\\InspectionItemInputAssistantWindow.exe " + tmpFile + " " + cCode);
                try {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(tmpFile), CONST_STR_SHIFT_JIS));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    String json = sb.toString();
                    Gson gson = new GsonBuilder().setDateFormat(CONST_STR_DATEFORMAT).create();
                    InspectionItems iItems = gson.fromJson(json, InspectionItems.class);
                    if (iItems != null && iItems.InspectionItems != null && iItems.InspectionItems.size() > 0) {
                        HashMap<String, String> lCls = iItems.getLargeClassification() != null
                                ? new HashMap<String, String>(iItems.getLargeClassification()) : null;
                        HashMap<String, String> mCls = iItems.getMediumClassification() != null
                                ? new HashMap<String, String>(iItems.getMediumClassification()) : null;
                        HashMap<String, String> sCls = iItems.getSmallClassification() != null
                                ? new HashMap<String, String>(iItems.getSmallClassification()) : null;
                        for (int i = 0; i < iItems.InspectionItems.size(); i++) {
                            HashMap<String, String> mHash = new HashMap<String, String>(iItems.InspectionItems.get(i));
                            if (lCls != null) {
                                mHash.putAll(lCls);
                            }
                            if (mCls != null) {
                                mHash.putAll(mCls);
                            }
                            if (sCls != null) {
                                mHash.putAll(sCls);
                            }
                            result.add(mHash);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    throw e;
                } catch (Exception e) {
                    throw e;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    delete(new File(tmpDir.toString()));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    */
    
    @SuppressWarnings("resource")
    public ArrayList<HashMap<String, String>> backOpenIIIDialog(int cCode) {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            String fileName = null;
            File dir = CONST_IS_DEBUG ? new File("C:\\work\\pleiades\\eclipse\\eln\\jar\\")
                    : new File(ExAPI.class.getClassLoader().getResource(".").getFile().toString());
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName() != null && file.getName().contains("httpRequest")) {
                    fileName = file.getName();
                    break;
                }
            }
            if (fileName != null) {
                Path tmpDir = null;
                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                try {
                    tmpDir = Files.createTempDirectory(null);
                    String tmpFile = tmpDir + "\\tmp" + (CONST_SIMPLEDATEFORMAT_MS.format(new Date())) + ".txt";
                    File f = new File(dir.getAbsolutePath(), fileName);
                    JarFile jf = new JarFile(f);
                    ZipEntry ze = jf.getEntry("httpRequest/InspectionItemInputAssistantWindow.zip");
                    decode(new ZipInputStream(jf.getInputStream(ze)), tmpDir.toString());
                    cmdExec(tmpDir + "\\Release\\InspectionItemInputAssistantWindow.exe " + tmpFile + " " + cCode);
                    try {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(new FileInputStream(tmpFile), CONST_STR_SHIFT_JIS));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();
                        String json = sb.toString();
                        Gson gson = new GsonBuilder().setDateFormat(CONST_STR_DATEFORMAT).create();
                        InspectionItems iItems = gson.fromJson(json, InspectionItems.class);
                        if (iItems != null && iItems.InspectionItems != null && iItems.InspectionItems.size() > 0) {
                            HashMap<String, String> lCls = iItems.getLargeClassification() != null
                                    ? new HashMap<String, String>(iItems.getLargeClassification()) : null;
                            HashMap<String, String> mCls = iItems.getMediumClassification() != null
                                    ? new HashMap<String, String>(iItems.getMediumClassification()) : null;
                            HashMap<String, String> sCls = iItems.getSmallClassification() != null
                                    ? new HashMap<String, String>(iItems.getSmallClassification()) : null;
                            for (int i = 0; i < iItems.InspectionItems.size(); i++) {
                                HashMap<String, String> mHash = new HashMap<String, String>(
                                        iItems.InspectionItems.get(i));
                                if (lCls != null) {
                                    mHash.putAll(lCls);
                                }
                                if (mCls != null) {
                                    mHash.putAll(mCls);
                                }
                                if (sCls != null) {
                                    mHash.putAll(sCls);
                                }
                                result.add(mHash);
                            }
                        }
                    } catch (OutOfMemoryError e) {
                        throw e;
                    } catch (Exception e) {
                        throw e;
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (Exception e2) {
                    }
                    try {
                        out.close();
                    } catch (Exception e2) {
                    }
                    try {
                        delete(new File(tmpDir.toString()));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int cmdExec(String cmd) {
        int result = 1;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            result = p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public void _saveFile(InputStream input, String outputFilePath, String code) {
        // FileOutputStreamオブジェクト生成（出力ファイルの指定）
        FileOutputStream fo;
        try {
            fo = new FileOutputStream("C:\\work\\file.csv");
            // OutputStreamWriterオブジェクト生成（文字コードの指定）
            OutputStreamWriter ow = new OutputStreamWriter(fo, "Windows-31J");//UnicodeLittle
            // 書き出す内容をセット
            ow.write("Hello ³!!");
            
            // ストリームの解放
            ow.close();
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void saveFile(String input, String outputFilePath, String code) {
        OutputStreamWriter output = null;
        try {
            output = new OutputStreamWriter(new FileOutputStream(outputFilePath), code);
            output.write(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e2) {
            }
            try {
                output.close();
            } catch (Exception e2) {
            }
        }
    }
    
    public void ___saveFile(InputStream input, String outputFilePath, String code) {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), code));
            int c;
            while ((c = input.read()) != -1)
                output.write(c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e2) {
            }
            try {
                output.close();
            } catch (Exception e2) {
            }
        }
    }
    
    public void decode(ZipInputStream zipIn, String aOutDir) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        
        try {
            File outDir = new File(aOutDir);
            outDir.mkdirs();
            ZipEntry entry = null;
            while ((entry = zipIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    String relativePath = entry.getName();
                    outDir = new File(outDir, relativePath);
                    outDir.mkdirs();
                } else {
                    String relativePath = entry.getName();
                    File outFile = new File(outDir, relativePath);
                    File parentFile = outFile.getParentFile();
                    parentFile.mkdirs();
                    fileOut = new FileOutputStream(outFile);
                    byte[] buf = new byte[256];
                    int size = 0;
                    while ((size = zipIn.read(buf)) > 0) {
                        fileOut.write(buf, 0, size);
                    }
                    fileOut.close();
                    fileOut = null;
                }
                zipIn.closeEntry();
                outDir = new File(aOutDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (Exception e) {
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    private static void delete(File f) {
        if (f.exists() == false) {
            return;
        }
        if (f.isFile()) {
            f.delete();
            
        } else if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
            f.delete();
        }
    }
    
}
