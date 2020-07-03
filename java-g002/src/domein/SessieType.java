package domein;

public enum SessieType {
    AANGEMAAKT,OPEN,GESTART,AFGELOPEN;

    @Override
    public String toString() {
        String name = super.toString().toLowerCase();
        char first = name.charAt(0);
        return name.replaceFirst(String.valueOf(first), String.valueOf(first).toUpperCase());
    }
}
