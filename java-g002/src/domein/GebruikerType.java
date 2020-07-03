package domein;

public enum GebruikerType {
    HOOFDVERANTWOORDELIJKE, VERANTWOORDELIJKE, GEBRUIKER;
    @Override
    public String toString() {
        String name = super.toString().toLowerCase();
        char first = name.charAt(0);
        return name.replaceFirst(String.valueOf(first), String.valueOf(first).toUpperCase());
    }
}
