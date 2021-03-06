package com.stone.service;


import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;

/**
 * Created by shidonghua on 2017/12/29.
 */
@Service
public class Html2Pdf {
    private static final String BASE_URL = "file:///Users/shidonghua/IdeaProjects/vm2pdf/src/main/resources/";

    public byte[] convert(String htmlStr) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setBaseURL(BASE_URL);
//        renderer.getSharedContext().getUserAgentCallback().setBaseURL("file:///Users/shidonghua/IdeaProjects/vm2pdf/src/main/resources/");
        ITextFontResolver fontResolver = renderer.getFontResolver();
        try {
            fontResolver.addFont("/Users/shidonghua/IdeaProjects/vm2pdf/src/main/resources/fonts/SimSun.ttf",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // for macbookpro user
//            fontResolver.addFont("/Users/shidonghua/Library/Fonts/SimSun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlStr, BASE_URL);
        System.out.println("baseurl:" + renderer.getSharedContext().getUserAgentCallback().getBaseURL());
        renderer.layout();
        try {
            renderer.createPDF(os);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return os.toByteArray();
    }


    public static void main(String[] args) throws IOException {
        Html2Pdf html2Pdf = new Html2Pdf();

        ClassLoader classLoader = html2Pdf.getClass().getClassLoader();
//        File htmlFile = new File(classLoader.getResource("html/htmlTest1.html").getFile());
        File htmlFile = new File(classLoader.getResource("html/contract1.html").getFile());

        // encoding type is must, otherwise chinese will not display properly
        String htmlStr = FileUtils.readFileToString(htmlFile, "utf-8");
//        byte[] content = html2Pdf.convert("<html><body>test pdf</body></html>");
        byte[] content = html2Pdf.convert(htmlStr);

        FileOutputStream fstream = null;
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File("test.pdf");
            fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);

            stream.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeQuietly(fstream);

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
