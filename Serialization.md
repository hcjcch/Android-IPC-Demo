## Serializable
 实现**Serializable**接口就可以实现序列化的功能了。
 #### 注意点 SerialVersionUID
 SerialVersionUID 可以使用IDE计算生成的，也可以使用自己定义的1L，2L等等，这两种方式没有任何区别。如果没有指定SerialVersionUID，这个类任何属性方法的改变都会导致序列化的失败。如果指定了SerialVersionUID，则在一些属性和方法的修改后，反序列化时会最大程度的恢复数据
 指定了transient的属性和静态变量不会参与序列化
 
 重写下面4个方法可以改变序列化的一些行为
 ```
    /**
      * 序列化时,
      * 首先系统会先调用writeReplace方法,在这个阶段,
      * 可以进行自己操作,将需要进行序列化的对象换成我们指定的对象.
      * 一般很少重写该方法
      * @return
      * @throws ObjectStreamException
      */
     private Object writeReplace() throws ObjectStreamException {
         System.out.println("writeReplace invoked");
         return this;
     }
     /**
      *接着系统将调用writeObject方法,
      * 来将对象中的属性一个个进行序列化,
      * 我们可以在这个方法中控制住哪些属性需要序列化.
      * 这里只序列化name属性
      * @param out
      * @throws IOException
      */
     private void writeObject(java.io.ObjectOutputStream out) throws IOException {
         System.out.println("writeObject invoked");
         out.writeObject(this.name == null ? "zejian" : this.name);
     }
 
     /**
      * 反序列化时,系统会调用readObject方法,将我们刚刚在writeObject方法序列化好的属性,
      * 反序列化回来.然后通过readResolve方法,我们也可以指定系统返回给我们特定的对象
      * 可以不是writeReplace序列化时的对象,可以指定其他对象.
      * @param in
      * @throws IOException
      * @throws ClassNotFoundException
      */
     private void readObject(java.io.ObjectInputStream in) throws IOException,
             ClassNotFoundException {
         System.out.println("readObject invoked");
         this.name = (String) in.readObject();
         System.out.println("got name:" + name);
     }
 
 
     /**
      * 通过readResolve方法,我们也可以指定系统返回给我们特定的对象
      * 可以不是writeReplace序列化时的对象,可以指定其他对象.
      * 一般很少重写该方法
      * @return
      * @throws ObjectStreamException
      */
     private Object readResolve() throws ObjectStreamException {
         System.out.println("readResolve invoked");
         return this;
     }
 ```
## Parcelable