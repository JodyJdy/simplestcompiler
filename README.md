# simplestcompiler
一个小型解释器

# 语法规则
## 变量
### 支持变量类型
* void
* int
* int[]
* string
* string[]

### 变量定义
```java
//无初始化数组定义
int[5] a;
//int变量定义
int i=1,j;
//有初始化数组定义
int[] array = {1,2,3,4};
//string变量定义
string s = "hello";
// string数组定义
string sarray = {"a","b","c"};
```
## 语法
###

程序执行入口是main函数，语句以分号结束,函数不需要在main函数之前声明

```java
void main()
{
    int i,j,k;
    print(i,j,j);
}
```

### 函数定义
```java
int add(int a,int b,int c){
  return a + b + c; 
}
```

### while
```java
int i = 0;
while (i < 10) {
    print(i);
    i = i + 1;
}
```

### for
```java

int i=0;
for(i=0;i<10;i=i+1){
    print(i);
}

```

### do while
```java

int i=0;
do{
    print(i);
    i=i+1;
}while(i<10)

```


### if-else

```java
int i,j;
if(i>0 && j> 0) {
    print(1);
} else if(j==5) {
    print(0);
} else {
    print(2);
}
```