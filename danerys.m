
fid = fopen('dane');
data = textscan(fid,'%f %f %f','Delimiter',';','CollectOutput',1);
data = data{:};
fid = fclose(fid);

%utworzenie osobnych macierzy z czasem, wysokoscia, pr�dko�ci� 
t=data(:,1);
h=data(:,2);
v=data(:,3);
%wykonanie wykres�w wysokosci od czasu, pr�dko�ci od czasu 
figure;
subplot(211); plot(t,h);
title('h od czasu');
subplot(212); plot(t,v);
title('predkosc od czasu');
