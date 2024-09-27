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
def insert_data(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
                INSERT INTO bus (id, bus_n)
                VALUES (%s, %s);
            """, (row[0], row[1]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0


def insert_data2(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
                INSERT INTO busStation (id, busStationName, chukou, station_id) 
                 VALUES (%s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

def insert_data3(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
               INSERT INTO cards (code, money, create_time)
                   VALUES (%s, %s, %s);
            """, (row[0], row[1], row[2]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

def insert_data4(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
               INSERT INTO connB (bus_id,Bstation_id)
                   VALUES (%s, %s);
            """, (row[0], row[1]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0
def insert_data5(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
               INSERT INTO connection (line_No, station_No, station_n)
                    VALUES (%s, %s, %s);
            """, (row[0], row[2], row[3]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

def insert_data6(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
               INSERT INTO lines (line_name,start_time,end_time,intro,mileage,color,first_opening,url)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s);
            """, (row[0],row[2], row[3], row[4], row[5], row[6], row[7], row[8]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0
def insert_data7(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
              INSERT INTO outInfo (id, out_info, outNo ,ttext, station_id)
                    VALUES (%s, %s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3], row[4]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0
def insert_data8(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
              INSERT INTO passengers (name,id_number,phone_number,gender,district)
                    VALUES (%s, %s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3], row[4]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

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
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

def insert_data92(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            if len(row[0]) == 9:
              cursor.execute("""
              INSERT INTO rides2 (user_ride2_id ,start_station,end_station,price,start_time,end_time)
                    VALUES (%s, %s, %s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3], row[4], row[5]))
              total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0
def insert_data10(conn, data):
    try:
        cursor = conn.cursor()
        total_records = 0
        for row in data:
            cursor.execute("""
             INSERT INTO stations (station_name,district,intro,chinese_name)
                     VALUES (%s, %s, %s, %s);
            """, (row[0], row[1], row[2], row[3]))
            total_records += 1
        conn.commit()

        return total_records
    except psycopg2.Error as e:
        conn.rollback()
        print("Failed to insert data.")
        print(e)
        return 0  # 如果插入失败，返回0

# 主函数
def main():
    file_path1 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\bus.csv'
    file_path2 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\busInfo.csv'
    file_path3 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\cards.csv'
    file_path4 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\connB.csv'
    file_path5 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\connection.csv'
    file_path6 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\lines.csv'
    file_path7 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\outInfo.csv'
    file_path8 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\passenger.csv'
    file_path9 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\ride.csv'
    file_path10 = 'D:\\Program Files\\PyCharm 2023.3.3\\HappyTry\\stations.csv'

    conn = open_connection()
    all_records = 0  # 初始化 all_records
    if conn:
        data = load_csv(file_path1)
        if data:
            total_records = insert_data(conn, data)
            conn.commit()  # 提交事务
            print("bus Total records loaded:", total_records)
            all_records += total_records

        data10 = load_csv(file_path10)
        if data10:
            total_records = insert_data10(conn, data10)
            conn.commit()
            print("stations Total records loaded:", total_records)
            all_records += total_records

        data8 = load_csv(file_path8)
        if data8:
            total_records = insert_data8(conn, data8)
            conn.commit()  # 提交事务
            print("passengers Total records loaded:", total_records)
            all_records += total_records

        data7 = load_csv(file_path7)
        if data7:
            total_records = insert_data7(conn, data7)
            conn.commit()  # 提交事务
            print("outInfo Total records loaded:", total_records)
            all_records += total_records

        data2 = load_csv(file_path2)
        if data2:
            total_records = insert_data2(conn, data2)
            conn.commit()  # 提交事务
            print("busInfo Total records loaded:", total_records)
            all_records += total_records

        data3 = load_csv(file_path3)
        if data3:
            total_records = insert_data3(conn, data3)
            conn.commit()  # 提交事务
            print("cards Total records loaded:", total_records)
            all_records += total_records

        data4 = load_csv(file_path4)
        if data4:
            total_records = insert_data4(conn, data4)
            conn.commit()  # 提交事务
            print("connB Total records loaded:", total_records)
            all_records += total_records

        data6 = load_csv(file_path6)
        if data6:
            total_records = insert_data6(conn, data6)
            conn.commit()  # 提交事务
            print("lines Total records loaded:", total_records)
            all_records += total_records

        data5 = load_csv(file_path5)
        if data5:
            total_records = insert_data5(conn, data5)
            conn.commit()  # 提交事务
            print("connection Total records loaded:", total_records)
            all_records += total_records

        data9 = load_csv(file_path9)
        if data9:
            total_records = insert_data9(conn, data9)
            conn.commit()  # 提交事务
            print("rides Total records loaded:", total_records)
            all_records += total_records

        if data9:
            total_records = insert_data92(conn, data9)
            conn.commit()  # 提交事务
            print("rides2 Total records loaded:", total_records)
            all_records += total_records

        conn.close()
    return all_records

if __name__ == "__main__":
    start_time = time.time()
    all_records = main()  # 接收返回值
    end_time = time.time()
    elapsed_time = end_time - start_time

    print("All record:", all_records)

    print("Loading speed:", all_records / elapsed_time, "records/s")
