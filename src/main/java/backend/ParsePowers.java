package backend;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ParsePowers {

    boolean isId = false;
    boolean isName = false;
    boolean isSquad = false;
    boolean isLevel = false;
    boolean isPosition = false;
    boolean isAge = false;
    boolean isGender = false;

    public List<Power> parseFromFile(String s) throws Exception {
        List<Power> powers = new ArrayList<>();
        Power power = null;


        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(s));

        int eventType = parser.getEventType();

      
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            if (eventType == XmlPullParser.START_TAG) {
                
                if (tagName.equals("BleachPower")) {
                    power = new Power();
                } else if (tagName.equals("id")) {
                    isId = true;
                } else if (tagName.equals("name")) {
                    isName = true;
                } else if (tagName.equals("squad")) {
                    isSquad = true;
                } else if (tagName.equals("level")) {
                    isLevel = true;
                } else if (tagName.equals("position")) {
                    isPosition = true;
                } else if (tagName.equals("age")) {
                    isAge = true;
                } else if (tagName.equals("gender")) {
                    isGender = true;
                }
            } else if (eventType == XmlPullParser.END_TAG) {
           
                if (tagName.equals("id")) {
                    isId = false;
                } else if (tagName.equals("name")) {
                    isName = false;
                } else if (tagName.equals("squad")) {
                    isSquad = false;
                } else if (tagName.equals("level")) {
                    isLevel = false;
                } else if (tagName.equals("position")) {
                    isPosition = false;
                } else if (tagName.equals("age")) {
                    isAge = false;
                } else if (tagName.equals("gender")) {
                    isGender = false;
                } else if (tagName.equals("BleachPower")) {
                    if (power != null) {
                        powers.add(power);  
                    }
                    power = null;  
                }
            }

  
            if (eventType == XmlPullParser.TEXT) {
                String text = parser.getText().trim();
                if (!text.isEmpty() && power != null) {
                    try {
   
                        if (isId) {
                            power.setId(Integer.parseInt(text));  
                        } else if (isName) {
                            power.setName(text);
                        } else if (isSquad) {
                            power.setSquad(text);
                        } else if (isLevel) {
                            power.setLevel(Integer.parseInt(text)); 
                        } else if (isPosition) {
                            power.setPosition(text);
                        } else if (isAge) {
                            power.setAge(Integer.parseInt(text));  
                        } else if (isGender) {
                            power.setGender(text);
                        }
                    } catch (NumberFormatException e) {
             
                        if (isId || isLevel || isAge) {
                            if (isId) {
                                power.setId(0);
                            } else if (isLevel) {
                                power.setLevel(0);
                            } else if (isAge) {
                                power.setAge(0);
                            }
                        }
                    }
                }
            }

            eventType = parser.next();  
        }

        return powers;
    }
}