<a href="https://gitlab.com/muhammadkholidb/sequel/-/pipelines" target="_blank"><img alt="pipeline status" src="https://gitlab.com/muhammadkholidb/sequel/badges/master/pipeline.svg" /></a> 
<a href="https://gitlab.com/muhammadkholidb/sequel/-/jobs" target="_blank"><img alt="coverage report" src="https://gitlab.com/muhammadkholidb/sequel/badges/master/coverage.svg" /></a> 

# Sequel

SQL helpers for Spring's JDBCTemplate


#### How to Use
1. Add JitPack maven repository

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. Add this project as maven dependency

```xml
<dependency>
    <groupId>com.gitlab.muhammadkholidb</groupId>
    <artifactId>sequel</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

3. Import `SequelConfig` to your configuration with DataSource

```java
...
import com.gitlab.muhammadkholidb.sequel.config.SequelConfig;

@Configuration
@Import(SequelConfig.class)
public class ApplicationConfig {

    @Bean
    public DataSource dataSource() {
        // ...
    }
}
```

4. Extend the `DataModel` to your data model classes


```java
...
import com.gitlab.muhammadkholidb.sequel.annotation.DataColumn;
import com.gitlab.muhammadkholidb.sequel.model.DataModel;

public class Product extends DataModel {

    @DataColumn("code")
    private String code;

    @DataColumn("name")
    private String name;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

This `DataModel` will provide the getters and setters for `id`, `createdAt`, `updatedAt`, `deletedAt` fields representing columns `id`, `created_at`, `updated_at`, `deleted_at`, which are required by Sequel to run. Make sure your tables already have those columns. Use `@DataColumn` to set other column names to your model fields.


5. Extend the `CommonRepository` and `AbstractRepository` to your repository classes

```java
...
import com.gitlab.muhammadkholidb.sequel.repository.CommonRepository;

public interface ProductRepository extends CommonRepository<Product> {

}

...
import com.gitlab.muhammadkholidb.sequel.repository.AbstractRepository;

@Repository
public class ProductRepositoryImpl extends AbstractRepository<Product> implements ProductRepository {

}
```

6. Use Sequel methods to query your database

```java
...
import com.gitlab.muhammadkholidb.sequel.sql.Where;

...

@Autowired
private ProductRepository productRepository;

public Long createProduct(String code, String name) {
    Product product = new Product();
    product.setCode(code);
    product.setName(name);
    return productRepository.create(product);   // Returns the generated id
}

public List<Product> getAllProducts() {
    return productRepository.read();
}

public Product getProductById(Long id) {
    return productRepository.readOne(id).orElse(null);
}

public List<Product> getProductByName(String name) {
    return productRepository.read(new Where().containsIgnoreCase("name", name));
}

public Integer updateProduct(Long id, String name) {
    Product product = productRepository.readOne(id);
    product.setName(name);
    return productRepository.update(product);   // Returns the number of rows affected
}

public void deleteProductById(Long id) {
    return productRepository.delete(id);    // Returns the number of rows affected
}

public void deleteProductByCode(String code) {
    return productRepository.delete(new Where().equals("code", code));  // Returns the number of rows affected
}
```

By default, Sequel will do a soft delete by setting the value to column `deleted_at`. There are always methods to do a hard delete by providing `true` value to the `force` param of the `delete()` method. 

The same is also applied for `read()` / `readOne()` methods. They will return data that is not softly deleted (with column `deleted_at` is null). You can also include the deleted data by providing `true` value to the `includeDeleted` param of `read()` / `readOne()` methods.
