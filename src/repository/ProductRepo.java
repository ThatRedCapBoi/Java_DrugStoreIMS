/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repository;

import java.util.List;
import java.util.Optional;
import model.Product;

/**
 *
 * @author Itadori
 */
public interface ProductRepo {

    List<Product> findAll();

    Optional<Product> findById(long id);

    Product save(Product p);

    void update(Product p);

    void delete(long id);

    List<Product> search(String query);

    boolean existsBySku(String sku);

    int countAll();

    int countLowStock(int threshold);

    java.util.List<model.Product> findLowStock(int threshold);

    java.util.List<dto.ReportRow> findByFilter(dto.ReportFilter filter);
}
