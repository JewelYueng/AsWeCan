package org.k2.processmining.support.normal.transform;

//import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TempLine {
    public int itemWidth=20;
    public String OriginalLine; //用以存放整条记录
    public String nameValSeparator; //数据项名与数据项值之间的分隔符，只对有数据项名日志有效
    public String itemSeparator; //数据项之间的分隔符
    Map<String,String> ItemMap; //由数据项标识与数据项值构成
    public String nulVal; //空值使用的字符串
    public TempLine(String originalLine, String nameValSeparator, String itemSeparator, String nulVal) {//有数据项名构造函数
        super();
        this.OriginalLine = originalLine;
        this.nameValSeparator = nameValSeparator;
        this.itemSeparator = itemSeparator;
        this. nulVal = nulVal;
        ItemMap = new LinkedHashMap<String, String>();

        String[] tempList = originalLine.split(itemSeparator);

        if(nulVal==null)
        for(int i=0;i<tempList.length;i++){
            String[] tempDataStrings = tempList[i].split(nameValSeparator);
            if(!tempDataStrings[1].equals(""))
            ItemMap.put(tempDataStrings[0], tempDataStrings[1]);
        }else
            for(int i=0;i<tempList.length;i++){
            String[] tempDataStrings = tempList[i].split(nameValSeparator);
            if(tempDataStrings[1].equals(nulVal))tempDataStrings[1]="";
            ItemMap.put(tempDataStrings[0], tempDataStrings[1]);
        }

    }
    public TempLine(String originalLine, String itemSeparator, String nulVal) {//无数据项名构造函数
        super();
        this.OriginalLine = originalLine;
        this.nameValSeparator = null;
        this.itemSeparator = itemSeparator;
        this. nulVal = nulVal;
        ItemMap = new LinkedHashMap<String, String>();

        String[] tempList = originalLine.split(itemSeparator);

        if(nulVal==null){
        for(int i=0;i<tempList.length;i++)
            if(!tempList[i].equals(""))
            ItemMap.put(i+"", tempList[i]);}
            else{
                for(int i=0;i<tempList.length;i++){
                    if(tempList[i].equals(nulVal))tempList[i]="";
                    ItemMap.put(i+"", tempList[i]);
                }
            }
    }
    public String getOriginalLine() {
        return OriginalLine;
    }
    public void setOriginalLine(String originalLine) {
        OriginalLine = originalLine;
    }
    public String getNameValSeparator() {
        return nameValSeparator;
    }
    public void setNameValSeparator(String nameValSeparator) {
        this.nameValSeparator = nameValSeparator;
    }
    public String getItemSeparator() {
        return itemSeparator;
    }
    public void setItemSeparator(String itemSeparator) {
        this.itemSeparator = itemSeparator;
    }

    public Map<String, String> getItemMap() {
        return ItemMap;
    }
    public void setItemMap(Map<String, String> itemMap) {
        ItemMap = itemMap;
    }

//		public void remove(String[] keys){//去除冗余数据项
//			for(int i=0;i<keys.length;i++)
//			if(ItemMap.containsKey(keys[i]))
//				ItemMap.remove(keys[i]);
//				else System.out.println("去除冗余-没有数据项:"+keys[i]);
//		}

    public String getNulVal() {
        return nulVal;
    }
    public void setNulVal(String nulVal) {
        this.nulVal = nulVal;
    }
    public void modifyValue(String key,String value){
        if(ItemMap.containsKey(key)){
            ItemMap.remove(key);
            ItemMap.put(key, value);
        }else System.out.println("修改-没有数据项:"+key);
    }
    public void renameORmerge(Map<String, String[]> renameORmergeItems){//合并完时间再合并其他项

        Map<String,String> tempMap  = new LinkedHashMap<String,String>();

        if(!ItemMap.containsKey("Time")){
            System.out.println("仍未处理时间项！");
            System.exit(0);
        }

        Iterator it1 = ItemMap.keySet().iterator();  //将ItemMap复制到tempMap
        while(it1.hasNext()) {
             String key = (String)it1.next();
             tempMap.put(key, ItemMap.get(key));
        }

        ItemMap.clear();
        ItemMap.put("Time", tempMap.get("Time"));


        Iterator it2 = renameORmergeItems.keySet().iterator();
        while(it2.hasNext()) {
            String key = (String)it2.next();
            String[] names = (String[]) renameORmergeItems.get(key);
//	            int n=0;//统计有效的数据项，若为0则此<k,v>无效
            String temp="";
            for(int i=0;i<names.length;i++)
                if(tempMap.containsKey(names[i])){
//	            		n++;
                    temp=temp+tempMap.get(names[i]);
                    tempMap.remove(names[i]);
                }//else System.out.println("renameORmerge无数据项名："+names[i]);

//	            if(n!=0) ItemMap.put(key,temp);
            ItemMap.put(key,temp);

        }

    }


    public void mergeTimeItems(String[] timeItems){
    String temp="";
        for(int i=0;i<timeItems.length;i++){
            if(ItemMap.containsKey(timeItems[i])){
                temp=temp+ItemMap.get(timeItems[i]);
                ItemMap.remove(timeItems[i]);
            }//else System.out.println("mergeTimeItems无数据项名："+timeItems[i]);
        }
        ItemMap.put("Time", temp);
    }

    public String changeItemWidth(String oriString){
        String normString="";
        if(oriString.length()>itemWidth){
            System.out.println(oriString);
            System.out.println("警告：目标数据项名/值超出标准长度");
            return oriString;
        }
        else {
            int toAdd=itemWidth-oriString.length();
            for(int i=0;i<toAdd;i++ ){
                oriString=oriString+" ";
            }
            normString=oriString;
        }

        return normString;
    }

    public String generateNewLine(String newItemSeparator,String newNulVal) throws IllegalArgumentException{
        if(newNulVal==null)
            throw new IllegalArgumentException("空值使用的字符串不可为空！");

        String temp=ItemMap.get("Time");
        Iterator it = ItemMap.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            if(!key.equals("Time"))
            {
                String keyValue = ItemMap.get(key);
                if(keyValue.equals(""))keyValue = newNulVal;
                temp=temp+newItemSeparator+keyValue;
            }
        }
        return temp;
    }
    public String generateNewLineWithWidth(String newItemSeparator,String newNulVal) throws Exception{
        if(newNulVal==null)
            throw new Exception("空值使用的字符串不可为空！");

        String temp=changeItemWidth(ItemMap.get("Time"));
        Iterator it = ItemMap.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            if(!key.equals("Time"))
            {
                String keyValue = ItemMap.get(key);
                if(keyValue.equals(""))keyValue = newNulVal;
                temp=temp+newItemSeparator+changeItemWidth(keyValue);
            }
        }
        return temp;
    }

    public String generateItemNamesLine(String newItemSeparator){
        String temp ="Time";
        Iterator it = ItemMap.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            if(!key.equals("Time"))
                temp = temp+newItemSeparator+key;
        }
        return temp;
    }
    public String generateItemNamesLineWithWidth(String newItemSeparator){
        String temp =changeItemWidth("Time");
        Iterator it = ItemMap.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            if(!key.equals("Time"))
                temp = temp+newItemSeparator+changeItemWidth(key);
        }
        return temp;
    }



public static void main(String args[]) throws Exception{
    String originalLine="[QC]:2015-01-01-21:00:00	[INFO]:Log4j	[Method]:update";
    String nameValSeparator=":";
    String itemSeparator="\t";
    String[] tempList = originalLine.split(itemSeparator);

    TempLine tempLine=new TempLine( originalLine,  nameValSeparator, itemSeparator, null);
    System.out.println(tempLine.generateNewLine(";","null"));
    System.out.println(tempLine.generateItemNamesLine(";"));
}
}
