## java2

Данная папка репозитория содержит выполненные мной задания по курсу "Парадигмы программирования". 

В данном курсе только несколько первых заданий были на языке Java.
Далее будут описаны задачи только на языке Java. Все остальные будут представлены без описания.

Исходный источник заданий -> [тут](https://web.archive.org/web/20221129113516/http://www.kgeorgiy.info/courses/paradigms/homeworks.html)

Исходники тестов к заданиям -> [пока тут ничего нет]()

---
#### <a id="tasks-list">Список представленных в репозитории выполненных работ:</a>
- [Бинарный поиск](#bin-search)
- [Очередь на массиве](#array-queue)
- [Очереди](#queues)
- [Остальные задания](#remainings)

---

### <a id="bin-search">Бинарный поиск</a>
[Вернуться к списку заданий](#tasks-list)
1. Реализуйте итеративный и рекурсивный варианты бинарного поиска в массиве.
2. На вход подается целое число x и массив целых чисел ```a```, отсортированный по невозрастанию. Требуется найти минимальное значение индекса ```i```, при котором ```a[i] <= x```.
3. Для ```main```, функций бинарного поиска и вспомогательных функций должны быть указаны, пред- и постусловия. Для реализаций методов должны быть приведены доказательства соблюдения контрактов в терминах троек Хоара.
4. Интерфейс программы.
   - Имя основного класса — ```search.BinarySearch```.
   - Первый аргумент командной строки — число ```x```. 
   - Последующие аргументы командной строки — элементы массива ```a```. 
5. Пример запуска: ```java search.BinarySearch 3 5 4 3 2 1```. Ожидаемый результат: ```2```.

Решение данного задания можно посмотреть [здесь](java-solutions/search) в папке ```java-solutions/search```.

---

### <a id="array-queue">Очередь на массиве</a>
[Вернуться к списку заданий](#tasks-list)
1. Определите модель и найдите инвариант структуры данных «очередь». Определите функции, которые необходимы для реализации очереди. Найдите их пред- и постусловия, при условии что очередь не содержит null.
2. Реализуйте классы, представляющие **циклическую** очередь на основе массива.
   - Класс [```ArrayQueueModule```](java-solutions/queue/ArrayQueueModule.java) должен реализовывать один экземпляр очереди с использованием переменных класса.
   - Класс [```ArrayQueueADT```](java-solutions/queue/ArrayQueueADT.java) должен реализовывать очередь в виде абстрактного типа данных (с явной передачей ссылки на экземпляр очереди).
   - Класс [```ArrayQueue```](java-solutions/queue/ArrayQueue.java) должен реализовывать очередь в виде класса (с неявной передачей ссылки на экземпляр очереди).
   - Должны быть реализованы следующие функции (процедуры) / методы:
     - ```enqueue``` – добавить элемент в очередь;
     - ```element``` – первый элемент в очереди;
     - ```dequeue``` – удалить и вернуть первый элемент в очереди;
     - ```size``` – текущий размер очереди;
     - ```isEmpty``` – является ли очередь пустой;
     - ```clear``` – удалить все элементы из очереди.
   - Модель, инвариант, пред- и постусловия записываются в исходном коде в виде комментариев.
   - Обратите внимание на инкапсуляцию данных и кода во всех трех реализациях.
3. Напишите тесты к реализованным классам.

Решение данного задания можно посмотреть [здесь](java-solutions/search) в папке ```java-solutions/search```.

---

### <a id="queues">Очереди</a>
[Вернуться к списку заданий](#tasks-list)
1. Определите интерфейс очереди Queue и опишите его контракт.
2. Реализуйте класс ```LinkedQueue``` — очередь на связном списке.
3. Выделите общие части классов ```LinkedQueue``` и ```ArrayQueue``` в базовый класс ```AbstractQueue```.

Решение данного задания можно посмотреть в классах [```LinkedQueue```](java-solutions/queue/LinkedQueue.java), [```AbstractQueue```](java-solutions/queue/AbstractQueue.java), а также в интерфейсе [```Queue```](java-solutions/queue/Queue.java).

---

### <a id="remainings">Остальные задания</a>
Описание всех последующих задач можно посмотреть [тут](https://web.archive.org/web/20221129113516/http://www.kgeorgiy.info/courses/paradigms/homeworks.html).

- JavaScript
  - Функциональные выражения на JavaScript ([решение](javascript-solutions/functionalExpression.js) в ```javascript-solutions/functionalExpression.js```)
  - Объектные выражения на JavaScript ([решение](javascript-solutions/objectExpression.js) в ```javascript-solutions/objectExpression.js```)
  - Обработка ошибок на JavaScript ([решение](javascript-solutions/objectExpression.js) в ```javascript-solutions/objectExpression.js```)
- Clojure
  - Линейная алгебра на Clojure ([решение](clojure-solutions/linear.clj) в ```clojure-solutions/linear.clj```)
  - Функциональные выражения на Clojure ([решение](clojure-solutions/expression.clj) в ```clojure-solutions/expression.clj```)
  - Объектные выражения на Clojure ([решение](clojure-solutions/expression.clj) в ```clojure-solutions/expression.clj```)
- Prolog
  - Простые числа на Prolog ([решение](prolog-solutions/primes.pl) в ```prolog-solutions/primes.pl```)
  - Дерево поиска на Prolog ([решение](prolog-solutions/tree-map.pl) в ```prolog-solutions/tree-map.pl```)

---
