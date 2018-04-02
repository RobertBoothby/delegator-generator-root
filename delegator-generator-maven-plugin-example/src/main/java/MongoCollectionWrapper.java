import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoCollectionDelegator;

public class MongoCollectionWrapper<TDocument> implements MongoCollectionDelegator<TDocument> {

    private final MongoCollection<TDocument> wrappedCollection;

    public MongoCollectionWrapper(MongoCollection<TDocument> wrappedCollection) {
        this.wrappedCollection = wrappedCollection;
    }

    @Override
    public MongoCollection<TDocument> getMongoCollectionDelegate() {
        return this.wrappedCollection;
    }

    public static void main(String[] args) {
        MongoCollectionDelegator<Object> delegate = new MongoCollectionWrapper<>(null);
    }
}
