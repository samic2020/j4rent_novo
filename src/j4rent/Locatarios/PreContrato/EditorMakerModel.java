package j4rent.Locatarios.PreContrato;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Samic
 */
public class EditorMakerModel extends AbstractTableModel {
    private List<EditorMakerClass> dados = new ArrayList<>();
    private String[] colunas = {"dbcampo","campo","descrição"};
    
    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
    
    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object retorno = null;
        switch (columnIndex) {
            case 0:
                retorno = dados.get(rowIndex).getDbfield();
                break;
            case 1:
                retorno = dados.get(rowIndex).getEdfield();
                break;
            case 2:
                retorno = dados.get(rowIndex).getDescription();
                break;
        }
        return retorno;
    }    
    
    public void addRow(EditorMakerClass _class) {
        this.dados.add(_class);
        this.fireTableDataChanged();
    }
    
    public void delRow(int rowIndex) {
        this.dados.remove(rowIndex);
        this.fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void clear() {
        this.dados.clear();
        this.fireTableDataChanged();
    }
}
