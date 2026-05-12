package com.aventurasoft.bocaplus.data.exporters;

import com.aventurasoft.bocaplus.data.entity.Socio;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SocioExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Socio> listSocios;

    public SocioExcelExporter(List<Socio> listSocios)
    {
        this.listSocios = listSocios;
        workbook = new XSSFWorkbook();

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Socios");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "id", style);
        createCell(row, 1, "numeroSocio", style);
        createCell(row, 2, "nombre", style);
        createCell(row, 3, "apellido", style);
        createCell(row, 4, "dni", style);
        createCell(row, 5, "activo", style);
        createCell(row, 6, "fechaNacimiento", style);
        createCell(row, 7, "genero", style);
        createCell(row, 8, "direccion", style);
        createCell(row, 9, "altura", style);
        createCell(row, 10, "piso", style);
        createCell(row, 11, "departamento", style);
        createCell(row, 12, "barrioId", style);
        createCell(row, 13, "localidadId", style);
        createCell(row, 14, "provinciaId", style);
        createCell(row, 15, "categoriaSocioId", style);
        createCell(row, 16, "tipoSocio", style);
    }
    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Socio socio : listSocios) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, socio.getId(), style);
            createCell(row, columnCount++, socio.getNumeroSocio(), style);
            createCell(row, columnCount++, socio.getNombre(), style);
            createCell(row, columnCount++, socio.getApellido(), style);
            createCell(row, columnCount++, socio.getDni(), style);
            createCell(row, columnCount++, socio.isActivo(), style);
            createCell(row, columnCount++, socio.getFechaNacimiento(), style);
            createCell(row, columnCount++, socio.getGenero(), style);
            createCell(row, columnCount++, socio.getDireccion(), style);
            createCell(row, columnCount++, socio.getAltura(), style);
            createCell(row, columnCount++, socio.getPiso(), style);
            createCell(row, columnCount++, socio.getDepartamento(), style);
            createCell(row, columnCount++, socio.getBarrioId(), style);
            createCell(row, columnCount++, socio.getLocalidadId(), style);
            createCell(row, columnCount++, socio.getProvinciaId(), style);
            createCell(row, columnCount++, socio.getCategoriaSocioId(), style);
            createCell(row, columnCount++, socio.getTipoSocio(), style);

        }
    }

    public void write(OutputStream outputStream) throws IOException
    {

        writeHeaderLine();
        writeDataLines();

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

}
