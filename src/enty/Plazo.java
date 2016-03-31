package enty;

/**
 *
 * @author Agarimo
 */
public enum Plazo {

    D10("10D"),
    D15("15D"),
    D20("20D"),
    M1("1M"),
    M2("2M");

    private final String value;

    Plazo(String value) {
        this.value=value;
    }
    
    public String getValue(){
        return this.value;
    }
    
    
     @Override
    public String toString() {
        switch (this) {
            case D10:
                return "10 días";
            case D15:
                return "15 días";
            case D20:
                return "20 días";
            case M1:
                return "1 mes";
            case M2:
                return "2 meses";
            default:
                throw new IllegalArgumentException();
        }
    }

}
