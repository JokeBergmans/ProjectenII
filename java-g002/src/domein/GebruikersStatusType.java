package domein;

public enum GebruikersStatusType {
    ACTIEF, GEBLOKKEERD,NIET_ACTIEF;

    @Override
    public String toString() {
        String name = super.toString().toLowerCase();
        char first = name.charAt(0);
        return name.replaceFirst(String.valueOf(first), String.valueOf(first).toUpperCase());
    }
}
