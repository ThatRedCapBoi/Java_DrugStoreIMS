package app;

import controller.AuthController;
import controller.CategoryController;
import controller.DataExchangeController;
import controller.ProductController;
import infra.DBManager;
import repository.CategoryRepo;
import repository.MySqlCategoryRepo;
import repository.MySqlUserRepo;
import repository.UserRepo;
import service.AuthService;
import service.AuthServiceImpl;
import service.CategoryService;
import service.CategoryServiceImpl;
import view.LoginView;
import javax.swing.SwingUtilities;
import repository.MySqlProductRepo;
import repository.ProductRepo;
import service.DataExchangeService;
import service.DataExchangeServiceImpl;
import service.ProductService;
import service.ProductServiceImpl;
import controller.DashboardController;
import service.DashboardService;
import service.DashboardServiceImpl;

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
        
        // Services
        AuthService authService = new AuthServiceImpl(userRepo);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepo);
        ProductService productService = new ProductServiceImpl(productRepo);
        DataExchangeService dataExchangeService = new DataExchangeServiceImpl(productRepo);
        DashboardService dashboardService = new DashboardServiceImpl(productRepo);
        
        // Controllers
        AuthController authController = new AuthController(authService);
        CategoryController categoryController = new CategoryController(categoryService);
        ProductController productController = new ProductController(productService);
        DataExchangeController dataExchangeController = new DataExchangeController(dataExchangeService);
        DashboardController dashboardController = new DashboardController(dashboardService);
        
        // Start UI
        SwingUtilities.invokeLater(() -> {
            new LoginView(authController, categoryController, productController, dataExchangeController, dashboardController).setVisible(true);
        });
    }
}
