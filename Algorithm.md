# Algorithm

## 算法

 算法是一个定义良好的计算过程，它将一些值作为输入并产生相应的输出值。简单来说，它是将输入转换为输出的一系列计算步骤。 

##  **快速排序算法** 

快速排序算法能够快速排序列表或查询。它基于分割交换排序的原则，这种类型的算法占用空间较小，它将待排序列表分为三个主要部分：

- 小于Pivot的元素
- 枢轴元素Pivot（选定的比较值）
- 大于Pivot的元素

##  **时间复杂度** 

 算法的时间复杂度表示程序运行完成所需的总时间，它通常用大O表示法来表示。 

##  **时间复杂度的符号类型** 

用于时间复杂度的符号类型包括：

- Big Oh：它表示小于或等于目标多项式
- Big Omega：它表示大于或等于目标多项式
- Big Theta：它表示与目标多项式相等
- Little Oh：它表示小于目标多项式
- Little Omega：它表示大于目标多项式

##  **二分法检索** 

 在二分法检索中，我们先确定数组的中间位置，然后将要查找的值与数组中间位置的值进行比较，若小于数组中间值，则要查找的值应位于该中间值之前，依此类推，不断缩小查找范围，直至得到最终结果。 

###  **是否可以使用二分法检索链表** 

 由于随机访问在链表中是不可接受的，所以不可能到达O（1）时间的中间元素。因此，对于链表来说，二分法检索是不可以的（对顺序链表或排序后的链表是可以用的）。 

##  **堆排序**

 堆排序可以看成是选择排序的改进，它可以定义为基于比较的排序算法。它将其输入划分为未排序和排序的区域，通过不断消除最小元素并将其移动到排序区域来收缩未排序区域。 

##  **Skip list** 

 Skip list数据结构化的方法，它允许算法在符号表或字典中搜索、删除和插入元素。在Skip list中，每个元素由一个节点表示。搜索函数返回与key相关的值的内容。插入操作将指定的键与新值相关联，删除操作可删除指定的键。 

 **插入排序算法的空间复杂度** 

 插入排序是一种就地排序算法，这意味着它不需要额外的或仅需要少量的存储空间。对于插入排序，它只需要将单个列表元素存储在初始数据的外侧，从而使空间复杂度为O（1）。 

##  **哈希算法**

 “哈希算法”是一个哈希函数，它使用任意长度的字符串，并将其减少为唯一的固定长度字符串。它用于密码有效性、消息和数据完整性以及许多其他加密系统。 

##  **如何查找链表是否有循环** 

 要知道链表是否有循环，我们将采用两个指针的方法。如果保留两个指针，并且在处理两个节点之后增加一个指针，并且在处理每个节点之后，遇到指针指向同一个节点的情况，这只有在链表有循环时才会发生。 

##  **解释加密算法的工作原理** 

 加密是将明文转换为称为“密文”的密码格式的过程。要转换文本，算法使用一系列被称为“键”的位来进行计算。密钥越大，创建密文的潜在模式数越多。大多数加密算法使用长度约为64到128位的固定输入块，而有些则使用流方法。 

##  **常用的加密算法** 

- 3-way
- Blowfish
- CAST
- CMEA
- GOST
- DES 和Triple DES
- IDEA
- LOKI等等

##  **算法的最佳情况和最坏情况之间有什么区别** 

最佳情况：算法的最佳情况解释为算法执行最佳的数据排列。例如，我们进行二分法检索，如果目标值位于正在搜索的数据中心，则这就是最佳情况，最佳情况时间复杂度为0。

·最差情况：给定算法的最差输入参考。例如快速排序，如果选择关键值的子列表的最大或最小元素，则会导致最差情况出现，这将导致时间复杂度快速退化到O（n2）。

##  **基数排序算法** 

 基数排序又称“桶子法”，是通过比较数字将其分配到不同的“桶里”来排序元素的。它是线性排序算法之一。 

##  **递归算法** 

 递归算法是一个解决复杂问题的方法，将问题分解成较小的子问题，直到分解的足够小，可以轻松解决问题为止。通常，它涉及一个调用自身的函数。 

###  **递归算法的三个定律** 

1. 递归算法必须有一个基点
2. 递归算法必须有一个趋向基点的状态变化过程
3. 递归算法必须自我调用

 **冒泡排序算法** 

 冒泡排序算法也称为下沉排序。在这种类型的排序中，要排序的列表的相邻元素之间互相比较。如果它们按顺序排列错误，将交换值并以正确的顺序排列，直到最终结果“浮”出水面。 

## 十大经典排序算法

### 算法复杂度

![](img/suanfao.png)

### **相关概念**

- **稳定**：如果a原本在b前面，而a=b，排序之后a仍然在b的前面。
- **不稳定**：如果a原本在b的前面，而a=b，排序之后 a 可能会出现在 b 的后面。
- **时间复杂度**：对排序数据的总的操作次数。反映当n变化时，操作次数呈现什么规律。
- **空间复杂度：**是指算法在计算机内执行时所需存储空间的度量，它也是数据规模n的函数。 

### 冒泡排序（Bubble Sort）

 冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。  

#### 算法描述

- 比较相邻的元素。如果第一个比第二个大，就交换它们两个；
- 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
- 针对所有的元素重复以上的步骤，除了最后一个；
- 重复步骤1~3，直到排序完成。

#### 代码实现

```
function bubbleSort(arr) {
    varlen = arr.length;
    for(vari = 0; i < len - 1; i++) {
        for(varj = 0; j < len - 1 - i; j++) {
            if(arr[j] > arr[j+1]) {        // 相邻元素两两对比
                vartemp = arr[j+1];        // 元素交换
                arr[j+1] = arr[j];
                arr[j] = temp;
            }
        }
    }
    returnarr;
}
```

### 选择排序（Selection Sort）

选择排序(Selection-sort)是一种简单直观的排序算法。它的工作原理：首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。 

#### 算法描述

n个记录的直接选择排序可经过n-1趟直接选择排序得到有序结果。具体算法描述如下：

- 初始状态：无序区为R[1..n]，有序区为空；
- 第i趟排序(i=1,2,3…n-1)开始时，当前有序区和无序区分别为R[1..i-1]和R(i..n）。该趟排序从当前无序区中-选出关键字最小的记录 R[k]，将它与无序区的第1个记录R交换，使R[1..i]和R[i+1..n)分别变为记录个数增加1个的新有序区和记录个数减少1个的新无序区；
- n-1趟结束，数组有序化了。

#### 代码实现

```
function selectionSort(arr) {
    varlen = arr.length;
    varminIndex, temp;
    for(vari = 0; i < len - 1; i++) {
        minIndex = i;
        for(varj = i + 1; j < len; j++) {
            if(arr[j] < arr[minIndex]) {     // 寻找最小的数
                minIndex = j;                 // 将最小数的索引保存
            }
        }
        temp = arr[i];
        arr[i] = arr[minIndex];
        arr[minIndex] = temp;
    }
    returnarr;
} 
```

#### 算法分析

 表现最稳定的排序算法之一，因为无论什么数据进去都是O(n2)的时间复杂度，所以用到它的时候，数据规模越小越好。唯一的好处可能就是不占用额外的内存空间了吧。理论上讲，选择排序可能也是平时排序一般人想到的最多的排序方法了吧。 

### 插入排序（Insertion Sort）

 插入排序（Insertion-Sort）的算法描述是一种简单直观的排序算法。它的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。 

#### 算法描述

一般来说，插入排序都采用in-place在数组上实现。具体算法描述如下：

- 从第一个元素开始，该元素可以认为已经被排序；
- 取出下一个元素，在已经排序的元素序列中从后向前扫描；
- 如果该元素（已排序）大于新元素，将该元素移到下一位置；
- 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
- 将新元素插入到该位置后；
- 重复步骤2~5。

#### 代码实现

```
function insertionSort(arr) {
    varlen = arr.length;
    varpreIndex, current;
    for(vari = 1; i < len; i++) {
        preIndex = i - 1;
        current = arr[i];
        while(preIndex >= 0 && arr[preIndex] > current) {
            arr[preIndex + 1] = arr[preIndex];
            preIndex--;
        }
        arr[preIndex + 1] = current;
    }
    returnarr;
}
```

#### 算法分析

 插入排序在实现上，通常采用in-place排序（即只需用到O(1)的额外空间的排序），因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。 

### 希尔排序（Shell Sort）

 1959年Shell发明，第一个突破O(n2)的排序算法，是简单插入排序的改进版。它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫**缩小增量排序**。 

#### 算法描述

先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：

- 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
- 按增量序列个数k，对序列进行k 趟排序；
- 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。

#### 代码实现

```
`// 修改于 2019-03-06``function shellSort(arr) {``  ``var``len = arr.length;``  ``for``(``var``gap = Math.floor(len / 2); gap > 0; gap = Math.floor(gap / 2)) {``    ``// 注意：这里和动图演示的不一样，动图是分组执行，实际操作是多个分组交替执行``    ``for``(``var``i = gap; i < len; i++) {``      ``var``j = i;``      ``var``current = arr[i];``      ``while``(j - gap >= 0 && current < arr[j - gap]) {``         ``arr[j] = arr[j - gap];``         ``j = j - gap;``      ``}``      ``arr[j] = current;``    ``}``  ``}``  ``return``arr;``}`
```

#### 算法分析

希尔排序的核心在于间隔序列的设定。既可以提前设定好间隔序列，也可以动态的定义间隔序列。动态定义间隔序列的算法是《算法（第4版）》的合著者Robert Sedgewick提出的。　

### 归并排序（Merge Sort）

归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。 

#### 算法描述

- 把长度为n的输入序列分成两个长度为n/2的子序列；
- 对这两个子序列分别采用归并排序；
- 将两个排序好的子序列合并成一个最终的排序序列。

#### 代码实现

```
function mergeSort(arr) {
    varlen = arr.length;
    if(len < 2) {
        returnarr;
    }
    varmiddle = Math.floor(len / 2),
        left = arr.slice(0, middle),
        right = arr.slice(middle);
    returnmerge(mergeSort(left), mergeSort(right));
}
function merge(left, right) {
    varresult = [];
 
    while(left.length>0 && right.length>0) {
        if(left[0] <= right[0]) {
            result.push(left.shift());
        } else{
            result.push(right.shift());
        }
    }
 
    while(left.length)
        result.push(left.shift());
 
    while(right.length)
        result.push(right.shift());
 
    returnresult;
}
```

#### 算法分析

归并排序是一种稳定的排序方法。和选择排序一样，归并排序的性能不受输入数据的影响，但表现比选择排序好的多，因为始终都是O(nlogn）的时间复杂度。代价是需要额外的内存空间。

### 快速排序（Quick Sort）

 快速排序的基本思想：通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。 

#### 法描述

快速排序使用分治法来把一个串（list）分为两个子串（sub-lists）。具体算法描述如下：

- 从数列中挑出一个元素，称为 “基准”（pivot）；
- 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
- 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。

#### 代码实现

```
function quickSort(arr, left, right) {
    varlen = arr.length,
        partitionIndex,
        left = typeofleft != 'number'? 0 : left,
        right = typeofright != 'number'? len - 1 : right;
 
    if(left < right) {
        partitionIndex = partition(arr, left, right);
        quickSort(arr, left, partitionIndex-1);
        quickSort(arr, partitionIndex+1, right);
    }
    returnarr;
}
function partition(arr, left ,right) {     // 分区操作
    varpivot = left,                      // 设定基准值（pivot）
        index = pivot + 1;
    for(vari = index; i <= right; i++) {
        if(arr[i] < arr[pivot]) {
            swap(arr, i, index);
            index++;
        }       
    }
    swap(arr, pivot, index - 1);
    returnindex-1;
}
 
function swap(arr, i, j) {
    vartemp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```

### 堆排序（Heap Sort）

堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。

#### 算法描述

- 将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区；
- 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
- 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。

#### 代码实现

```
varlen;    // 因为声明的多个函数都需要数据长度，所以把len设置成为全局变量
 
function buildMaxHeap(arr) {   // 建立大顶堆
    len = arr.length;
    for(vari = Math.floor(len/2); i >= 0; i--) {
        heapify(arr, i);
    }
}
 
function heapify(arr, i) {     // 堆调整
    varleft = 2 * i + 1,
        right = 2 * i + 2,
        largest = i;
 
    if(left < len && arr[left] > arr[largest]) {
        largest = left;
    }
 
    if(right < len && arr[right] > arr[largest]) {
        largest = right;
    }
 
    if(largest != i) {
        swap(arr, i, largest);
        heapify(arr, largest);
    }
}
 
function swap(arr, i, j) {
    vartemp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
 
function heapSort(arr) {
    buildMaxHeap(arr);
 
    for(vari = arr.length - 1; i > 0; i--) {
        swap(arr, 0, i);
        len--;
        heapify(arr, 0);
    }
    returnarr;
}
```

### 计数排序（Counting Sort）

计数排序不是基于比较的排序算法，其核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。 作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。

#### 算法描述

- 找出待排序的数组中最大和最小的元素；
- 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
- 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
- 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。

#### 代码实现

```
function countingSort(arr, maxValue) {
    varbucket = newArray(maxValue + 1),
        sortedIndex = 0;
        arrLen = arr.length,
        bucketLen = maxValue + 1;
 
    for(vari = 0; i < arrLen; i++) {
        if(!bucket[arr[i]]) {
            bucket[arr[i]] = 0;
        }
        bucket[arr[i]]++;
    }
 
    for(varj = 0; j < bucketLen; j++) {
        while(bucket[j] > 0) {
            arr[sortedIndex++] = j;
            bucket[j]--;
        }
    }
 
    returnarr;
}
```

#### 算法分析

计数排序是一个稳定的排序算法。当输入的元素是 n 个 0到 k 之间的整数时，时间复杂度是O(n+k)，空间复杂度也是O(n+k)，其排序速度快于任何比较排序算法。当k不是很大并且序列比较集中时，计数排序是一个很有效的排序算法。

### 桶排序（Bucket Sort）

桶排序是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。桶排序 (Bucket sort)的工作的原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排）。

####  算法描述

- 设置一个定量的数组当作空桶；
- 遍历输入数据，并且把数据一个一个放到对应的桶里去；
- 对每个不是空的桶进行排序；
- 从不是空的桶里把排好序的数据拼接起来。 

#### 代码实现

```
function bucketSort(arr, bucketSize) {
    if(arr.length === 0) {
      returnarr;
    }
 
    vari;
    varminValue = arr[0];
    varmaxValue = arr[0];
    for(i = 1; i < arr.length; i++) {
      if(arr[i] < minValue) {
          minValue = arr[i];                // 输入数据的最小值
      } elseif(arr[i] > maxValue) {
          maxValue = arr[i];                // 输入数据的最大值
      }
    }
 
    // 桶的初始化
    varDEFAULT_BUCKET_SIZE = 5;            // 设置桶的默认数量为5
    bucketSize = bucketSize || DEFAULT_BUCKET_SIZE;
    varbucketCount = Math.floor((maxValue - minValue) / bucketSize) + 1;  
    varbuckets = newArray(bucketCount);
    for(i = 0; i < buckets.length; i++) {
        buckets[i] = [];
    }
 
    // 利用映射函数将数据分配到各个桶中
    for(i = 0; i < arr.length; i++) {
        buckets[Math.floor((arr[i] - minValue) / bucketSize)].push(arr[i]);
    }
 
    arr.length = 0;
    for(i = 0; i < buckets.length; i++) {
        insertionSort(buckets[i]);                      // 对每个桶进行排序，这里使用了插入排序
        for(varj = 0; j < buckets[i].length; j++) {
            arr.push(buckets[i][j]);                     
        }
    }
 
    returnarr;
}
```

#### 算法分析

桶排序最好情况下使用线性时间O(n)，桶排序的时间复杂度，取决与对各个桶之间数据进行排序的时间复杂度，因为其它部分的时间复杂度都为O(n)。很显然，桶划分的越小，各个桶之间的数据越少，排序所用的时间也会越少。但相应的空间消耗就会增大。 

### 基数排序（Radix Sort）

基数排序是按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前。

#### 算法描述

- 取得数组中的最大数，并取得位数；
- arr为原始数组，从最低位开始取每个位组成radix数组；
- 对radix进行计数排序（利用计数排序适用于小范围数的特点）；

#### 代码实现

```
varcounter = [];
function radixSort(arr, maxDigit) {
    varmod = 10;
    vardev = 1;
    for(vari = 0; i < maxDigit; i++, dev *= 10, mod *= 10) {
        for(varj = 0; j < arr.length; j++) {
            varbucket = parseInt((arr[j] % mod) / dev);
            if(counter[bucket]==null) {
                counter[bucket] = [];
            }
            counter[bucket].push(arr[j]);
        }
        varpos = 0;
        for(varj = 0; j < counter.length; j++) {
            varvalue = null;
            if(counter[j]!=null) {
                while((value = counter[j].shift()) != null) {
                      arr[pos++] = value;
                }
          }
        }
    }
    returnarr;
}
```

####  算法分析

基数排序基于分别排序，分别收集，所以是稳定的。但基数排序的性能比桶排序要略差，每一次关键字的桶分配都需要O(n)的时间复杂度，而且分配之后得到新的关键字序列又需要O(n)的时间复杂度。假如待排数据可以分为d个关键字，则基数排序的时间复杂度将是O(d*2n) ，当然d要远远小于n，因此基本上还是线性级别的。

基数排序的空间复杂度为O(n+k)，其中k为桶的数量。一般来说n>>k，因此额外空间需要大概n个左右。

## 七大查找算法

- **1. 顺序查找**

  说明：顺序查找适合于存储结构为顺序存储或链接存储的线性表。

  **基本思想**：顺序查找也称为线形查找，属于无序查找算法。从数据结构线形表的一端开始，顺序扫描，依次将扫描到的结点关键字与给定值k相比较，若相等则表示查找成功；若扫描结束仍没有找到关键字等于k的结点，表示查找失败。

  **复杂度分析**：　

  　　查找成功时的平均查找长度为：（假设每个数据元素的概率相等） ASL = 1/n(1+2+3+…+n) = (n+1)/2 ;
  　　当查找不成功时，需要n+1次比较，时间复杂度为O(n);

  　　所以，顺序查找的时间复杂度为O(n)。

- [**2. 二分查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label1)

  说明：元素必须是有序的，如果是无序的则要先进行排序操作。

  　　**基本思想**：也称为是折半查找，属于有序查找算法。用给定值k先与中间结点的关键字比较，中间结点把线形表分成两个子表，若相等则查找成功；若不相等，再根据k与该中间结点关键字的比较结果确定下一步查找哪个子表，这样递归进行，直到查找到或查找结束发现表中没有这样的结点。

  　　**复杂度分析**：最坏情况下，关键词比较次数为log2(n+1)，且期望时间复杂度为O(log2n)；

  　　注：折半查找的前提条件是需要**有序表顺序存储**，对于静态查找表，一次排序后不再变化，折半查找能得到不错的效率。但对于需要**频繁执行插入或删除操作**的数据集来说，维护有序的排序会带来不小的工作量，那就不建议使用。——《大话数据结构》

- [**3. 插值查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label2)

- [**4. 斐波那契查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label3)

- [**5. 树表查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label4)

- [**6. 分块查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label5)

- [**7. 哈希查找**](http://www.cnblogs.com/maybe2030/p/4715035.html#_label6)