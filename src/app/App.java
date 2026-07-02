package app;

import controller.AuthController;
import controller.CategoryController;
import controller.DataExchangeController;
import controller.DashboardController;
import controller.ProductController;
import controller.ReportController;
import controller.VendorController;
import infra.DBManager;
import repository.CategoryRepo;
import repository.MySqlCategoryRepo;
import repository.MySqlProductRepo;
import repository.MySqlUserRepo;
import repository.MySqlVendorRepo;
import repository.ProductRepo;
import repository.UserRepo;
import repository.VendorRepo;
import service.AuthService;
import service.AuthServiceImpl;
import service.CategoryService;
import service.CategoryServiceImpl;
import service.DashboardService;
import service.DashboardServiceImpl;
import service.DataExchangeService;
import service.DataExchangeServiceImpl;
import service.ProductService;
import service.ProductServiceImpl;
import controller.DashboardController;
import service.DashboardService;
import service.DashboardServiceImpl;
import service.ReportService;
import service.ReportServiceImpl;
import service.VendorService;
import service.VendorServiceImpl;
import view.LoginView;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Init FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Singleton infra
        DBManager db = DBManager.getInstance();

        // Repos
        UserRepo userRepo = new MySqlUserRepo(db);
        CategoryRepo categoryRepo = new MySqlCategoryRepo(db);
        ProductRepo productRepo = new MySqlProductRepo(db);
        VendorRepo vendorRepo = new MySqlVendorRepo(db);
        
        // Services
        AuthService authService = new AuthServiceImpl(userRepo);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepo);
        ProductService productService = new ProductServiceImpl(productRepo);
        DataExchangeService dataExchangeService = new DataExchangeServiceImpl(productRepo);
        ReportService reportService = new ReportServiceImpl(productRepo);
        VendorService vendorService = new VendorServiceImpl(vendorRepo);
        DashboardService dashboardService = new DashboardServiceImpl(productRepo, categoryRepo);
        
        // Controllers
        AuthController authController = new AuthController(authService);
        CategoryController categoryController = new CategoryController(categoryService);
        ProductController productController = new ProductController(productService);
        DataExchangeController dataExchangeController = new DataExchangeController(dataExchangeService);
        DashboardController dashboardController = new DashboardController(dashboardService);
        ReportController reportController = new ReportController(reportService);
        VendorController vendorController = new VendorController(vendorService);
        
        // Start UI
        SwingUtilities.invokeLater(() -> {
            new LoginView(authController, categoryController, productController, dataExchangeController, dashboardController, reportController, vendorController).setVisible(true);
        });
    }
}
