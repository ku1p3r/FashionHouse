package common.base;

// Use for things you may want to pick from in the terminal (i.e. retailers, products, etc.)
// Being an interface rather than an abstract class should make it relatively non-invasive.
public interface Selectable {
    String getId();
    String getName();
}
