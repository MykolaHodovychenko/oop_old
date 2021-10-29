#include <vector>
#include <functional>
#include <iostream>
#include <algorithm>
#include <ctime>
#include <numeric>
#include <random>



using namespace std;





// Task 1
vector<int> GenerateVector(const int start_range, const int end_range, int vector_length)
{

    if (vector_length < 0) { vector_length = 1; }

    // An instance of an engine
    random_device random_device;

    // Specify the engine and distribution
    mt19937 engine{ random_device() };
    uniform_int_distribution<int> distribution{ start_range, end_range };

    auto generator = [&distribution, &engine]() {	return distribution(engine); };


    vector<int> maybePrimes(vector_length);
    generate(begin(maybePrimes), end(maybePrimes), generator);


    return maybePrimes;

}
vector<int> CheckIfPrimeNumber(vector<int> vec)
{

    vector<int> ReallyPrimes;

    copy_if(vec.begin(), vec.end(), back_inserter(ReallyPrimes), [&](int value)
        {

            if (value <= 1) { return false; }


            for (int i = 2; i < value; i++)
            {
                if (value % i == 0) { return false; }
            }

            return true;

        });

    return ReallyPrimes;

}
void PrintIntVector(vector<int>vec)
{
    for_each(vec.begin(), vec.end(), [](const int a) {cout << a << " "; });
}




// Task 2
class Student
{

public:


    Student(const string name, const string group) :Name(name), Group(group) { this->Marks.reserve(0); }
    Student() :Student("", "") {}

    virtual ~Student() {}
   

public:


    const string& GetName()const { return this->Name; }
    const string& GetGroup()const { return this->Group; }
    const vector<int>& GetMarks()const { return this->Marks; }

    void SetName(const string name) { this->Name = name; }
    void SetGroup(const string group) { this->Group = group; }
    void SetMarks(const vector<int> marks) { this->Marks = marks; }

    void ShowStudentMarks()const
    {
        for_each(Marks.begin(), Marks.end(), [&](int mark) {cout << mark << " "; });
    }


private:


     string Name;
     string Group;

     vector<int> Marks;


};
vector<Student> GenerateStudents()
{

    vector<Student>vec;

    // A bit of hardcoding
    vec.push_back(Student{ "Denis","AI201" });
    vec.push_back(Student{ "Mark","AI201" });
    vec.push_back(Student{ "Nikola","AI201" });
    vec.push_back(Student{ "Lev","AI201" });


    for (size_t i = 0; i < vec.size(); i++)
    {
        vec[i].SetMarks(GenerateVector(70, 100, 10));
    }

    return vec;


}
vector<Student> FilterStudents(vector<Student> vec)
{

    vector<Student>CommonStudents;
    copy_if(vec.begin(), vec.end(), back_inserter(CommonStudents), [&](const Student student)
    {

            return all_of(student.GetMarks().begin(),
                          student.GetMarks().end(), [&](int mark) { return mark > 59; }); 

    });

    return CommonStudents;

}
void PrintCommonStudents(vector<Student> CommonStudents)
{

    for_each(CommonStudents.begin(), CommonStudents.end(), [&](const Student student)
        {

            cout << student.GetName() << " " << student.GetGroup() << " ";
            student.ShowStudentMarks();
            cout << endl;

        });
  
}




// Task 3
auto pred1 = [&](int a) {return a < 200; };
auto pred2 = [&](int a) {return a > 100; };
vector<int> FilterVectorByPredicates(function <bool(int)>pred1, function <bool(int)>pred2, vector<int>vec)
{
    
    vector<int>FilteredVector;
    copy_if(vec.begin(), vec.end(),back_inserter(FilteredVector), [&](int a)
        {
            return pred1(a) && pred2(a);
        });

    return FilteredVector;

}




// Task 4
void ShowStudentsByConsumer(vector<Student>vec)
{

    for_each(vec.begin(), vec.end(), [](const Student student1) { cout << student1.GetName() << " " << student1.GetGroup() << endl; });

}




// Task 5
auto isNerd = [&](Student student)
{
    return all_of(student.GetMarks().begin(), student.GetMarks().end(), [](const auto mark) {return mark > 70; });
};
auto defineDestiny = [&]()
{
    cout << "BOTAN" << endl;
};
vector<Student> ExtraFilter(function<bool(Student)>pred1, function <void(void)>pred2, vector<Student>vec)
{

    for_each(vec.begin(), vec.end(), [&](Student& student)
        {
            if (pred1(student)) { pred2(); }
        });

    return vec;
}




// Task 6
auto func = [&](int a)->int
{
    if (pow(2, a) > numeric_limits<int>::max() || pow(2, a) < numeric_limits<int>::min()) { return 0; }
    return pow(2, a);
};
vector<int> TransformVector(vector<int>vec, function<int(int)>func)
{
    transform(vec.begin(), vec.end(), vec.begin(), [&](int a) { return func(a); });

    return vec;
}




// Task 7
auto func2 = [&](int a)->string
{

    switch (a)
    {

    case 0: return "zero"; break;
    case 1: return "one"; break;
    case 2: return "two"; break;
    case 3: return "three"; break;
    case 4: return "four"; break;
    case 5: return "five"; break;
    case 6: return "six"; break;
    case 7: return "seven"; break;
    case 8: return "eight"; break;
    case 9: return "nine"; break;

    default: break;

    }

};
void stringify(vector<int> vec, function<string(int)>func)
{
    for_each(vec.begin(), vec.end(), [&](int a) {cout << func(a) << " "; });
}








int main()
{


    // PrintIntVector(CheckIfPrimeNumber(GenerateVector(0, 1000, 1000)));
    // PrintCommonStudents(FilterStudents(GenerateStudents()));
    // PrintIntVector(FilterVectorByPredicates(pred1, pred2, GenerateVector(70, 400, 40)));
    // ShowStudentsByConsumer(GenerateStudents());
    // PrintCommonStudents(ExtraFilter(isNerd, defineDestiny, GenerateStudents()));
    // PrintIntVector(TransformVector(GenerateVector(1, 50, 10),func));
    // stringify(GenerateVector(0, 9, 20), func2);


	return 0;

}
