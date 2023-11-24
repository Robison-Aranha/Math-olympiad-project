drop table if exists permissao cascade;
drop table if exists sala_pergunta cascade;
drop type if exists resposta cascade;
drop type if exists tema cascade;
drop type if exists tipo cascade;
drop table if exists pergunta cascade;
drop table if exists sala cascade;
drop table if exists usuario cascade;

create type resposta as enum ('A', 'B', 'C', 'D', 'E');
create type tema as enum ('POLUICAO_AMBIENTAL', 'BIODIVERSIDADE', 'DESENVOLVIMENTO_SUSTENTAVEL');
create type tipo as enum ('VERDADEIRO_OU_FALSO', 'PERGUNTA');


create table usuario (
	id bigint generated by default as identity not null,
	nome varchar(100) not null,
	imagem text not null,
	senha varchar(100) not null,
	ativo BOOLEAN not null,
	id_sala bigint
);
alter table usuario add constraint pk_usuario primary key (id);
alter table usuario add constraint uk_usuario_nome unique (nome);


create table permissao (
	id bigint generated by default as identity not null,
	nome varchar(100) not null,
	usuario_id BIGINT not null
);
alter table permissao add constraint pk_permissao primary key (id);
alter table permissao add constraint uk_permissao unique (nome, usuario_id);
alter table permissao add constraint fk_permissao_usuario foreign key (usuario_id) references usuario;


create table pergunta (
    id bigint generated by default as identity not null,
    link text not null,
    questao text not null,
    resposta resposta not null,
    tema tema not null,
    tipo tipo not null
);
alter table pergunta add constraint pk_pergunta primary key (id);

create table sala (
    id bigint generated by default as identity not null,
    nome varchar(10) not null,
    privado boolean not null,
    numero_rodadas int not null,
    numero_jogadores int not null,
    tempo_rodada int not null,
    senha varchar(20) not null
);
alter table sala add constraint pk_sala primary key (id);

create table placar (
    id bigint generated by default as identity not null,
    id_sala bigint not null,
    nome varchar(100) not null,
    pontos int not null
);
alter table placar add constraint pl_placar primary key (id);
alter table placar add constraint fk_placar_id_sala foreign key (id_sala) references sala;


create table sala_pergunta (
    id_sala bigint not null,
    id_pergunta bigint not null
);
alter table sala_pergunta add constraint fk_sala_pergunta_id_sala foreign key (id_sala) references sala;
alter table sala_pergunta add constraint fk_sala_pergunta_id_pergunta foreign key (id_pergunta) references pergunta;





