# SD-Project

## Description
Distributed Systems project instance

### Funcionalidades Adicionais

#### Limite de descargas:

- [x] O servidor deve garantir que em cada momento não há mais do que MAXDOWN descargas de ficheiros a ocorrer em simultâneo.

- [x] Pedidos de descarga que ultrapassem o limite devem esperar até obterem vez.

- [ ] Em caso de espera, deve ser estabelecida uma política que combine a ordem de chegada com a
necessidade de todos os utilizadores obterem ficheiros.

#### Notificação de novas músicas:

- [ ] Durante o período em que um cliente está ligado ao servidor, deve receber notificações de novas músicas que sejam carregadas.

- [ ] Cada notificação deve conter o título e autor da música.

- [ ] Deve ser possível continuar a receber e mostrar notificações mesmo durante outras operações
demoradas de carga ou descarga de ficheiros.

#### Tamanho dos ficheiros ilimitado:

- [x] A solução apresentada não deve assumir que cada ficheiro de música cabe completamente em
memória.

- [x] Pelo contrário, deve garantir que a transferência de cada ficheiro nunca ocupa mais do que MAXSIZE bytes em memória, tanto do cliente como do servidor