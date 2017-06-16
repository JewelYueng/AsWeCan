package org.k2.processmining.support.normal.transform;

import java.io.*;

public class Normalize {
    // don't need to close inputStream and outputStream because they should be closed where they were created ?
    public static boolean normalize(LogConfiguration LC, InputStream inputStream, OutputStream outputStream) {
        String nulVal=" ";
        String seperator="\t";
        int n = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0) {
                    TempLine tempLine;
                    if(LC.getNameValSeparator()==null)
                        tempLine = new TempLine(line,LC.getItemSeparator(),LC.getNulVal());
                    else
                        tempLine = new TempLine(line, LC.getNameValSeparator(), LC.getItemSeparator(),LC.getNulVal());
                    FormatInfo[] tempFormatInfos = LC.getFormatInfos();
                    for(int i=0;i<tempFormatInfos.length;i++){
                        FormatInfo tempFormatInfo = tempFormatInfos[i];
                        String temp = tempLine.getItemMap().get(tempFormatInfo.getItemNameOrIndex());
                        temp = tempFormatInfos[i].formatTransform(temp, tempLine.getItemMap().get(tempFormatInfos[i].getFormatTypeNameOrIndex()));
                        tempLine.modifyValue(tempFormatInfo.getItemNameOrIndex(), temp);
                    }
                    tempLine.mergeTimeItems(LC.getTimeItems());
                    tempLine.renameORmerge(LC.getRenameORmergeItems());

                    if(n==0)
                        writer.write(tempLine.generateItemNamesLine(seperator)+"\r\n");//写入文件首行
                    writer.write(tempLine.generateNewLine(seperator, nulVal)+"\r\n");
                    n++;
                }
            }
            // have to flush!!!!!!
            writer.flush();

            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
