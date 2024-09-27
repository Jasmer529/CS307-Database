import pandas as pd


def convert_excel_to_csv(input_file, output_file):
    # 读取原始Excel文件
    df = pd.read_excel(input_file, header=None)

    # 创建空的DataFrame来存储转换后的数据
    result_df = pd.DataFrame(columns=['x_station', 'y_station', 'priceB'])

    # 获取站点名称列表
    stations = df.iloc[2:, 2:].values.flatten()

    for i in range(3, len(df)):
        for j in range(3, len(df.columns)):
            x_station = df.iloc[i, 2]
            y_station = stations[j - 2]
            price = df.iloc[i, j]
            if not pd.isnull(price):
                # 检查是否已经存在相同的 (x, y) 对
                if not ((result_df['x_station'] == x_station) & (result_df['y_station'] == y_station)).any():
                    result_df = pd.concat(
                        [result_df, pd.DataFrame({'x_station': [x_station], 'y_station': [y_station], 'priceB': [price]})],
                        ignore_index=True)

    # 将结果保存到CSV文件
    result_df.to_csv(output_file, index=False)


if __name__ == "__main__":
    convert_excel_to_csv(r"D:\Program Files\PyCharm 2023.3.3\HappyTry\PriceB.xlsx", "priceOutB.csv")
