package ru.kolaer.server.webportal.model.servirce;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 13.10.2017.
 */
public interface ExcelReader<T> {
    Logger log = LoggerFactory.getLogger(ExcelReader.class);

    T parse(XSSFRow row, List<String> nameColumns);

    default T checkValues(XSSFRow row, T object) {
        return object;
    }

    default T process(XSSFRow row, List<String> nameColumns) {
        return checkValues(row, parse(row, nameColumns));
    }

    default String getStringValue(List<String> columns, String nameColumns, XSSFRow row) {
        return Optional.ofNullable(getCell(columns, nameColumns, row))
                .map(cell -> {
                    switch (cell.getCellTypeEnum()) {
                        case STRING: return cell.getStringCellValue().trim();
                        default:
                            throw new UnexpectedRequestParams(String.format("Значение в строке: %d и в столбце: %d - не является строкой",
                                row.getRowNum() + 1, cell.getColumnIndex() + 1));
                    }
                }).orElse(null);
    }

    default Date getDateValue(List<String> columns, String nameColumns, XSSFRow row) {
        return Optional.ofNullable(getCell(columns, nameColumns, row))
                .map(XSSFCell::getDateCellValue)
                .orElse(null);
    }

    default XSSFCell getCell(List<String> columns, String nameColumns, XSSFRow row) {
        int indexColumn = columns.indexOf(nameColumns);
        if(indexColumn > -1) {
            return row.getCell(indexColumn);
        } else {
            log.warn("Column: {} not found!", nameColumns);
        }

        return null;
    }
}
