package j4rent.Locatarios.PreContrato;

/**
 *
 * @author Samic
 */
public class EditorMakerClass {
    private String dbfield;
    private String edfield;
    private String description;

    public EditorMakerClass(String dbfield, String edfield, String description) {
        this.dbfield = dbfield;
        this.edfield = edfield;
        this.description = description;
    }

    public String getDbfield() { return dbfield; }
    public void setDbfield(String dbfield) { this.dbfield = dbfield; }

    public String getEdfield() { return edfield; }
    public void setEdfield(String edfield) { this.edfield = edfield; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }        
}
