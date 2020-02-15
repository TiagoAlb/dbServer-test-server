## Servidor de aplicação

Foi configurado o projeto em um servidor externo, para facilitar os testes da equipe. 

Link do projeto: http://ec2-18-229-155-222.sa-east-1.compute.amazonaws.com:3000/

Para testes de requisições a API: http://ec2-18-229-155-222.sa-east-1.compute.amazonaws.com:8080/

# Client-Side

A aplicação foi desenvolvida utilizando, para o lado do cliente, React na sua nova versão com uso de Hooks. Para instalção dos pacotes externos do projeto, é necessária a instalação do NodeJS. 

Veja mais em: [NodeJS](https://nodejs.org/en/)

Com o NodeJS instalado, rode o seguinte comando no diretório do projeto:
### `npm install`

Após a instalação dos pacotes o comando a baixo iniciará o servidor do lado do cliente:
### `npm start`

# Server-Side

Para o lado do servidor, foi utilizado o framework Spring Boot na versão 2.2.4 e o Maven para controle de dependências. Inicialmente, para facilitar a configuração do projeto a quem fosse testar, iniciou-se o desenvolvimento com gravação dos dados em arquivos txt em diretório. Porém, esta pratica estava tomando muito tempo, fazendo com que fosse alterado para o banco de dados. O banco de dados utilizado foi o MySQL, com utilização do Hibernate para criação de tabelas e comunicação com banco de dados.

### Banco de dados

Usuário: root
Senha: root

## Endpoints

Foram criados endpoints de cominicação do lado do servidor para comunicação entre cliente e server, dentre eles estão:
### GET:
Listar usuários disponíveis para votação: /api/

Listar restaurantes: /api/restaurants

Listar restaurantes da última votação com contagem de votos: /api/restaurants/voting

### POST:
Criar novo usuário: /api/users

### PUT:
Votar em um restaurante: /api/users/{userId}/restaurants/{restaurantId}/vote

Finalizar votação: /api/restaurants/voting/end

# Exceções de erro da API:
- Usuário não encontrado!
- Restaurante não encontrado!
- Erro ao buscar contagem de votos!
- Votação não encontrada!
- Este restaurante já ganhou a votação nesta semana!
- O usuário já realizou seu voto!

## Pesquisa de Restaurantes

A listagem dos restaurantes(raio de 2km da DBServer no Tecnopuc) foi realizada com base em uma pesquisa a API Places do Google. Abaixo estão alguns endpoints de exemplo nos testes para pesquisa:

Listagem de restaurantes:
https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=restaurant&radius=2000&location=-30.0596914,-51.173819&key=AIzaSyCECNy_clrtfjPtZqV9r3DFYeN2f7Se-r8

Busca por foto do estabelecimento:
https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=CmRaAAAAN3TvYIPdyPJIeE1tOXP9Gs681JjYZmsRMLWOL-fa1C7RPFTVh6wz_od9Mm1RPMMpEuoGqVWu6LD5jALrtT9vJqvaWzPTOt0ow1ZTrO01rBDqETUoMig0QRHbxCfIRezPEhAiWclnLe3IXjLeZtQiK9EZGhQCrREf3KGLTnWvCQZklNaUX9s6Gg&key=AIzaSyCECNy_clrtfjPtZqV9r3DFYeN2f7Se-r8

Busca de detalhes de um estabelecimento:
https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJG9681hp4GZURCWloUO0sHkg&fields=place_id,name,formatted_phone_number,photo,opening_hours,vicinity&key=AIzaSyCECNy_clrtfjPtZqV9r3DFYeN2f7Se-r8

Foi utilizado o Postman verificação de todos os endpoints da aplicação e externos. Caso desejem, posso compartilhar o workspace para o email de alguém (Já possui os objetos de envio também). Abaixo o link para a collection:
https://www.postman.com/collections/dd1b179ed4781c972972

## Funcionamento básico do sistema:

- O término da votação será reaizado as 11h e 30min de todos os dias, para que todos possam se despir de preconceitos e preparar seu psicologico antes do almoço.
- Ao acessar o sistema, caso a votação não esteja encerrada, será mostrado um timer com o tempo restante e lista(caso haja) de usuários que se cadastraram mas não votaram ainda. É possível também incluir um novo.
- Ao clicar em um usuário ou incluir um novo, o sistema irá direcionar para a tela de restaurantes, onde será mostado todos os restaurantes em um raio de 2km (para que todos possam voltar a tempo) do local de trabalho da equipe e um botão de coração para realizar a votação.
- Ao realizar a votação, o usuário será automaticamente direcionado para a tela de inicio e será informado os restaurantes já votados e quantos votos cada.
- Caso o usuário desista de votar, é possível retornar a tela anterior através do botão na barra ao topo da página.
- Ao término da votação, o timer é finalizado e o ganhador é informado.

## Desenvolvimentos futuros:

O sistema foi realizado buscando otimizar o tempo de desenvolvimento para entrega, portanto alguns pontos ainda ficaram faltando, o que seria interessante para realização futura:
- Paginação da listagem de restaurantes
- Aumento do raio de busca dos restaurantes
- Inclusão de barra de pesquisa de restaurantes
- Inclusão de estrelas para a votação da qualidade de cada restaurante
- Autenticação de usuário (para que ninguém vote na vez do outro) e inclusão de CPF para previnir usuários duplicados
- Roteamento das páginas na parte do cliente
- Inclusão de componentes para mensagens personalizadas na parte do cliente
- Inclusão de camada de segurança na parte do servidor, com controle de requisições
