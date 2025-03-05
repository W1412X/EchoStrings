package com.android.echostrings.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChordLearnData {
    private static HashMap<String,String>press_description;
    private static HashMap<String,String>play_description;
    private static HashMap<String,String>title;
    private static HashMap<String, ArrayList<String>>major_chord;
    private static ArrayList<String>majors;
    static {
        ArrayList<String> chordsList = new ArrayList<>();
        chordsList.add("c");
        chordsList.add("dm");
        chordsList.add("em");
        chordsList.add("f");
        chordsList.add("g");
        chordsList.add("am");
        major_chord=new HashMap<String,ArrayList<String>>();
        major_chord.put("c", chordsList);
        //chord
        majors=new ArrayList<>();
        majors.add("c");
        //title
        title=new HashMap<String,String>();
        title.put("c_c","C大调 C");
        title.put("c_dm","C大调 Dm");
        title.put("c_em","C大调 Em");
        title.put("c_f","C大调 F");
        title.put("c_g","C大调 G");
        title.put("c_am","C大调 Am");
        //press
        press_description=new HashMap<>();
        press_description.put("c_c", "食指按压2弦1品，中指按压4弦2品，无名指按压5弦3品。");
        press_description.put("c_dm", "食指按压1弦1品，中指按压2弦2品，无名指按压3弦2品。");
        press_description.put("c_em", "食指按压5弦2品，中指按压4弦2品，无名指按压3弦2品。");
        press_description.put("c_f", "食指横按1-6弦1品，中指按压3弦2品，无名指按压5弦3品，小指按压4弦3品。");
        press_description.put("c_g", "中指按压6弦3品，无名指按压5弦3品，食指按压5弦2品。");
        press_description.put("c_am", "食指按压2弦1品，中指按压4弦2品，无名指按压3弦2品。");
        //play
        play_description=new HashMap<>();
        play_description.put("c_am","右手自上而下(五弦到一弦)向下顺势拨弦(5s)");
        play_description.put("c_c","右手自上而下(六弦到一弦)向下顺势拨弦(5s)");
        play_description.put("c_dm","右手自上而下(四弦到一弦)向下顺势拨弦(5s)");
        play_description.put("c_em","右手自上而下(六弦到一弦)向下顺势拨弦(5s)");
        play_description.put("c_f","右手自上而下(六弦到一弦)向下顺势拨弦(5s)");
        play_description.put("c_g","右手自上而下(六弦到一弦)向下顺势拨弦(5s)");
    }
    public static String getPressDes(String major_chord){
        return press_description.get(major_chord);
    }
    public static String getPlayDescription(String major_chord){
        return play_description.get(major_chord);
    }
    public static String getTitle(String major_chord){
        return title.get(major_chord);
    }
    public static ArrayList<String>getChords(String major){
        return major_chord.get(major);
    }
    public static ArrayList<String>getMajors(){
        return majors;
    }

}
