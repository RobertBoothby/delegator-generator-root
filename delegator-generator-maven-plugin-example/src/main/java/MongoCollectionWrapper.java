import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoCollectionDelegator;
import org.bson.conversions.Bson;

/**
 * This class demonstrates multiple inheritance of delegation.
 */
public class MongoCollectionWrapper<TDocument, R, E extends Exception> implements MongoCollectionDelegator<TDocument>, AnInterfaceDelegator<TDocument, R, E> {

    private final MongoCollection<TDocument> wrappedCollection;
    private final AnInterface<TDocument, R, E> wrappedInterface;

    public MongoCollectionWrapper(MongoCollection<TDocument> wrappedCollection, AnInterface<TDocument, R, E> wrappedInterface) {
        this.wrappedCollection = wrappedCollection;
        this.wrappedInterface = wrappedInterface;
    }

    /**
     * This method has been implemented so that we can delegate all the MongoCollection methods to it..
     * @return the MongoCollection instance to delegate to.
     */
    @Override
    public MongoCollection<TDocument> getMongoCollectionDelegate() {
        return this.wrappedCollection;
    }

    /**
     * This method has been implemented so that we can delegate all the AnInteface methods to it..
     * @return the AnInterface instance to delegate to.
     */
    @Override
    public AnInterface<TDocument, R, E> getAnInterfaceDelegate() {
        return wrappedInterface;
    }

    @Override
    public void count(Bson filter, SingleResultCallback<Long> callback) {
        System.out.println("Point override of one delegation method working!");
        getMongoCollectionDelegate().count(filter, callback);
    }

    public static void main(String[] args) {
        MongoCollectionWrapper<Object, String, Exception> delegator = new MongoCollectionWrapper<>(null, null);

        //Invoking a MongoCollection method.
        delegator.find();

        //Invoking an AnInterface method.
        delegator.doSomethingElse();
    }
}
