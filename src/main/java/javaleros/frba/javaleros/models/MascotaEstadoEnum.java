package javaleros.frba.javaleros.models;

public enum MascotaEstadoEnum {
    PERDIDO("perdido"),
    ADOPTADO("adoptado"),
    ENADOPCION("enadopcion");
    private String value;

    MascotaEstadoEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
