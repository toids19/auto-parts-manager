package org.example.Repository;

import org.example.Connection.ConnectionFactory;
import org.example.Mapper.FieldMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Generic Data Access Object (DAO) for handling CRUD operations.
 *
 * @param <T> the type of entity managed by this repository
 */
@SuppressWarnings("ALL")
public class AbstractRepository<T> {
    /**
     * Logger for logging messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractRepository.class.getName());

    /**
     * Class of the entity managed by this DAO.
     */
    private final Class<T> entityClass;

    /**
     * Constructor to initialize the entity class.
     */
    public AbstractRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Find an entity by its ID.
     *
     * @param id the ID of the entity
     * @return the entity with the specified ID, or null if not found
     */
    public T findById(Long id) {
        String query = createFindByIdQuery(id);
        return executeQueryForSingleResult(query);
    }

    /**
     * Find all entities.
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        String query = createFindAllQuery();
        return executeQueryForMultipleResults(query);
    }

    /**
     * Insert a new entity.
     *
     * @param entity the entity to insert
     * @return the inserted entity
     */
    public T insert(T entity) {
        String query = createInsertQuery(entity);
        return executeInsert(entity, query);
    }

    /**
     * Update an entity by ID.
     *
     * @param id            the ID of the entity to update
     * @param updatedEntity the updated entity
     * @return the updated entity
     */
    public T updateById(Long id, T updatedEntity) {
        String query = createUpdateByIdQuery(id, updatedEntity);
        return executeUpdate(id, updatedEntity, query);
    }

    /**
     * Delete an entity by ID.
     *
     * @param id the ID of the entity to delete
     * @return the number of affected rows
     */
    public int deleteById(Long id) {
        String query = createDeleteByIdQuery(id);
        return executeDelete(query);
    }

    /**
     * Generate a query to find an entity by ID.
     *
     * @param id the ID of the entity
     * @return the query to find the entity by ID
     */
    private String createFindByIdQuery(Long id) {
        return String.format("SELECT * FROM %ss WHERE %s_id = %d;",
                entityClass.getSimpleName().toLowerCase(),
                entityClass.getSimpleName().toLowerCase(),
                id);
    }

    /**
     * Generate a query to find all entities.
     *
     * @return the query to find all entities
     */
    private String createFindAllQuery() {
        return String.format("SELECT * FROM %ss;", entityClass.getSimpleName().toLowerCase());
    }

    /**
     * Generate a query to insert a new entity.
     *
     * @param entity the entity to insert
     * @return the query to insert the entity
     */
    private String createInsertQuery(T entity) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
            columns.append(columnName).append(",");
            field.setAccessible(true);
            try {
                values.append("'").append(field.get(entity)).append("',");
            } catch (IllegalAccessException e) {
                LOGGER.severe("Error accessing field value: " + e.getMessage());
            }
        }

        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        return String.format("INSERT INTO %ss (%s) VALUES (%s);",
                entityClass.getSimpleName().toLowerCase(),
                columns,
                values);
    }

    /**
     * Generate a query to update an entity by ID.
     *
     * @param id            the ID of the entity to update
     * @param updatedEntity the updated entity
     * @return the query to update the entity
     */
    private String createUpdateByIdQuery(Long id, T updatedEntity) {
        StringBuilder setClause = new StringBuilder();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals(entityClass.getSimpleName().toLowerCase() + "Id")) {
                continue;
            }
            field.setAccessible(true);
            try {
                String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
                setClause.append(columnName).append("='").append(field.get(updatedEntity)).append("',");
            } catch (IllegalAccessException e) {
                LOGGER.severe("Error accessing field value: " + e.getMessage());
            }
        }

        setClause.setLength(setClause.length() - 1);

        return String.format("UPDATE %ss SET %s WHERE %s_id = %d;",
                entityClass.getSimpleName().toLowerCase(),
                setClause,
                entityClass.getSimpleName().toLowerCase(),
                id);
    }

    /**
     * Generate a query to delete an entity by ID.
     *
     * @param id the ID of the entity to delete
     * @return the query to delete the entity
     */
    private String createDeleteByIdQuery(Long id) {
        return String.format("DELETE FROM %ss WHERE %s_id = %d;",
                entityClass.getSimpleName().toLowerCase(),
                entityClass.getSimpleName().toLowerCase(),
                id);
    }

    /**
     * Execute a query and return a single result.
     *
     * @param query the query to execute
     * @return the single result of the query
     */
    public T executeQueryForSingleResult(String query) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return createObject(resultSet);
        } catch (SQLException e) {
            LOGGER.severe("Error executing query: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute a query and return multiple results.
     *
     * @param query the query to execute
     * @return a list of multiple results
     */
    public List<T> executeQueryForMultipleResults(String query) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.severe("Error executing query: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute an insert query and return the inserted entity.
     *
     * @param entity the entity to insert
     * @param query  the query to execute
     * @return the inserted entity
     */
    private T executeInsert(T entity, String query) {
        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Inserting entity failed, no rows affected.");
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return findById(id);
                } else {
                    LOGGER.severe("Inserting entity failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error executing insert: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute an update query and return the updated entity.
     *
     * @param id            the ID of the entity to update
     * @param updatedEntity the updated entity
     * @param query         the query to execute
     * @return the updated entity
     */
    private T executeUpdate(Long id, T updatedEntity, String query) {
        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Updating entity failed, no rows affected.");
                return null;
            }
            return updatedEntity;
        } catch (SQLException e) {
            LOGGER.severe("Error executing update: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute a delete query and return the number of affected rows.
     *
     * @param query the query to execute
     * @return the number of affected rows
     */
    private int executeDelete(String query) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error executing delete: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Create an entity instance from the result set.
     *
     * @param resultSet the result set to create the entity from
     * @return the created entity
     */
    private T createObject(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                T instance = entityClass.getDeclaredConstructor().newInstance();

                for (Field field : entityClass.getDeclaredFields()) {
                    String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
                    Object value = resultSet.getObject(columnName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), entityClass);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                return instance;
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating object: " + e.getMessage());
        }
        return null;
    }

    /**
     * Create a list of entity instances from the result set.
     *
     * @param resultSet the result set to create entities from
     * @return a list of created entities
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();

        try {
            while (resultSet.next()) {
                T instance = entityClass.getDeclaredConstructor().newInstance();

                for (Field field : entityClass.getDeclaredFields()) {
                    String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
                    Object value = resultSet.getObject(columnName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), entityClass);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating objects: " + e.getMessage());
        }
        return list;
    }
}
