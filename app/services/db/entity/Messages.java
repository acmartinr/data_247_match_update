package services.db.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

@XmlRootElement( name = "messages" )
public class Messages {
    private List< Message > messages;

    public Messages() {}

    public Messages(List< Message > messages ) { this.messages = messages; }

    @XmlElement( name = "message" )
    public List< Message > getMessages() { return messages; }
    public void setMessages( List< Message > messages ) { this.messages = messages; }

    public static class Message implements Comparable< Message > {
        private String msisdn;
        private int report;
        private int custom;
        private String value;

        public Message() {}

        public Message( String msisdn, int custom, String value ) {
            this( msisdn, /*1*/ 0, custom, value );
        }

        public Message( String msisdn, int report, int custom, String value ) {
            this.msisdn = msisdn;
            this.report = report;
            this.custom = custom;
            this.value = value;
        }

        @XmlAttribute
        public String getMsisdn() { return msisdn; }
        public void setMsisdn( String msisdn ) { this.msisdn = msisdn; }

        @XmlAttribute
        public int getReport() { return report; }
        public void setReport( int report ) { this.report = report; }

        @XmlAttribute
        public int getCustom() { return custom; }
        public void setCustom( int custom ) { this.custom = custom; }

        @XmlValue
        public String getValue() { return value; }
        public void setValue( String value ) { this.value = value; }

        @Override
        public int compareTo( Message o ) {
            return getCustom() - o.getCustom();
        }
    }
}