import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

# 定义拟合的非线性模型
def nonlinear_model(x, a, b, c):
    return a * np.log(b * x) + c

x_data = np.array([100, 1000, 10000, 50000, 63503])
y_data = np.array([480.18, 2815.73, 5567.65, 6140.30, 6166.02])


params, covariance = curve_fit(nonlinear_model, x_data, y_data)


a_fit, b_fit, c_fit = params


x_fit = np.linspace(min(x_data), max(x_data), 100)
y_fit = nonlinear_model(x_fit, a_fit, b_fit, c_fit)


plt.scatter(x_data, y_data, label='Original data')
plt.plot(x_fit, y_fit, 'r-', label='Fitted curve')
plt.xlabel('Records')
plt.ylabel('Speed')
plt.title('Nonlinear Curve Fitting of the speed')
plt.legend()
plt.show()

print("Parameters (a, b, c):", params)
