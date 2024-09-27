import csv
import psycopg2
import time

# 打开数据库连接
def open_connection():
    try:
        conn = psycopg2.connect(
            dbname='p2',
            user='postgres',
            password='guozi20040925',
            host='localhost',
            port='5432'
        )
        return conn
    except psycopg2.Error as e:
        print("Unable to connect to the database.")
        print(e)
        return None

# 加载 CSV 文件
def load_csv(file_path):
    try:
        with open(file_path, 'r', newline='', encoding='utf-8') as file:
            reader = csv.reader(file)
            next(reader)  # 跳过标题行
            data = list(reader)
            return data
    except FileNotFoundError:
        print(f"File {file_path} not found.")
        return None

# 将数据插入到数据库

def insert_data9(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            if len(row[0]) == 18:
                cursor.execute("""
              INSERT INTO rides (user_ride1_id ,start_station,end_station,price,start_time,end_time)
                   VALUES (%s, %s, %s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3], row[4], row[5]))
                total_records += 1
                if total_records == 100:
                    now_time = time.time()
                    print("The speed of 100 records is",100 / (now_time - start_time), "records/s")
                if total_records == 1000:
                    now_time = time.time()
                    print("The speed of 1000 records is",1000 / (now_time - start_time), "records/s")
                if total_records == 10000:
                    now_time = time.time()
                    print("The speed of 10000 records is",10000 / (now_time - start_time), "records/s")
                if total_records == 50000:
                    now_time = time.time()
                    print("The speed of 50000 records is",50000 / (now_time - start_time), "records/s")

        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

# 主函数
def main():
    file_path9 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\ride.csv'

    conn = open_connection()
    all_records = 0  # 初始化 all_records
    if conn:

        data9 = load_csv(file_path9)
        if data9:
            total_records = insert_data9(conn, data9)
            conn.commit()  # 提交事务
            print("rides Total records loaded:", total_records)
            all_records += total_records

    return all_records

if __name__ == "__main__":
    start_time = time.time()
    all_records = main()  # 接收返回值
    end_time = time.time()
    elapsed_time = end_time - start_time

    print("All record:", all_records)

    print("Loading speed:", all_records / elapsed_time, "records/s")
