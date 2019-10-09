package me.trow.tgclan;

public class CustomClassLoader extends ClassLoader {

    private final byte[] data;
    private final String name;

    public CustomClassLoader(ClassLoader parent, byte[] data, String name) {
        super(parent);
        this.data = data;
        this.name = name;
    }

    @Override
    public Class loadClass(String n) throws ClassNotFoundException {
        if (!name.equals(n)) {
            return super.loadClass(n);
        }
        try {
            return defineClass(name,
                    data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}