function [rcnd, x0, re0, rr0, xf, ref] = badsys(n)
% filling the matrices
a = zeros(n,n);
for i = 1:n
    for j = 1:n
        a(i,j) = j^i;
    end
end
b = zeros(n,1);
sol = zeros(n,1);
temp = 0;
for i = 1:n
    for j = 1:n
       temp = temp + ((-1)^(j+1))*a(i,j);
    end
    b(i) = temp;
    temp = 0;
    sol(i) = (-1)^(i+1);
end
% we know the solution already, so heres a definite solution
[L, U, P] = lu(a);
x0 = U\(L\b); % first solve
rr0 = norm(b-(a*sol), 2)/norm(b,2);
re0 = norm(x0-sol,2)/norm(sol,2);
xn = x0;
z = sol - xn;
re = b - a*(x0);
y = zeros(n,1);
count = 0;
disp(L)

while norm(z,2) > 0 && count < 10
    % might want to reimplement it as back/forward sub
    for i=1:n
        y(i) = b(i)/L(i,i);
        for j= 1:(i-1)
            y(i) = y(i) - L(i,j)*y(j)/L(i,i);
        end
    end

    for i=n:-1:1
        z(i) = y(i)/U(i,i);
        for j = (i+1):n
    	   U(i,j);
    	   z(i) = z(i) - U(i,j)*z(j)/U(i,i);
        end
    end
    xn = xn + z;
    z = sol - xn;
    re = b - a*(xn);
    count = count + 1;
end
xf = xn;
ref = norm(xn-sol,2)/norm(sol,2);
rcnd = rcond(a);
end
