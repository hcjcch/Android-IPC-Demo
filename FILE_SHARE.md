## File Share
文件共享是一种简单的进程间通信的方式，通过2个进程对同一个文件进行读写实现。比如A进程将信息写入文件，B进程将信息读出文件。文件不但能够交换一些文本文件还能够交换对象。A进程将一个对象序列化到一个文件中，B进程从文件中反序列化出来。但是本质上他还是**两个对象**。这也是有的单例模式并不是纯粹的单例的一个原因，因为序列化能够创造出另一个相同的对象。据**Effective Java**将枚举才能实现唯一的单例。
#### 优点
实现简单，易于理解。
#### 缺点
由于多个进程操作同一个文件就会存在并发读写的问题，所以我们要避免并发写文件。也因此文件共享适合数据同步要求不高的进程间通信。

### SharedPreferences文件
官方不建议使用**SharedPreferences**共享，因为它底层的原理是采用XML文件来存储键值对，所以会存在文件并发读写的问题。并且它本身有一种缓存策略，即内存中会有一份缓存，因此多进程模式下，它变得不再靠谱，高并发下，它数据丢失率比较高。