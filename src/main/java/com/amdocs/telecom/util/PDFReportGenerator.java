package com.amdocs.telecom.util;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.IOException;
import java.util.List;


public class PDFReportGenerator {


    private static final String REPORT_DIRECTORY =
            "reports/";



    public static String generatePDF(
            String fileName,
            String title,
            List<String> content
    ) {


        try {


            // Create reports folder if not exists

            File directory =
                    new File(
                            REPORT_DIRECTORY
                    );


            if(!directory.exists()) {

                directory.mkdirs();

            }



            String filePath =
                    REPORT_DIRECTORY +
                            fileName;



            PDDocument document =
                    new PDDocument();



            PDPage page =
                    new PDPage();



            document.addPage(
                    page
            );



            PDPageContentStream stream =
                    new PDPageContentStream(
                            document,
                            page
                    );



            stream.beginText();


            stream.setFont(
                    PDType1Font.HELVETICA_BOLD,
                    16
            );


            stream.newLineAtOffset(
                    50,
                    750
            );


            stream.showText(
                    title
            );


            stream.setFont(
                    PDType1Font.HELVETICA,
                    12
            );


            stream.newLineAtOffset(
                    0,
                    -30
            );



            for(String line : content) {


                stream.showText(
                        line
                );


                stream.newLineAtOffset(
                        0,
                        -20
                );
            }



            stream.endText();


            stream.close();


            document.save(
                    filePath
            );


            document.close();



            return filePath;



        } catch(IOException e) {


            throw new RuntimeException(
                    "Failed to generate PDF report.",
                    e
            );
        }

    }

}