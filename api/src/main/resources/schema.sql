drop table if exists permissao cascade;
drop table if exists token cascade;
drop table if exists sala_pergunta cascade;
drop table if exists sala_tema cascade;
drop type if exists tema_pergunta cascade;
drop type if exists tipo_pergunta cascade;
drop type if exists resposta cascade;
drop table if exists pergunta cascade;
drop table if exists tema cascade;
drop table if exists placar cascade;
drop table if exists recomecar_votacao cascade;
drop table if exists resposta_pergunta cascade;
drop table if exists resposta_avancar cascade;
drop table if exists usuario cascade;
drop table if exists sala cascade;

create type resposta as enum ('A', 'B', 'C', 'D', 'E');
create type tema_pergunta as enum ('POLUICAO_AMBIENTAL', 'BIODIVERSIDADE', 'DESENVOLVIMENTO_SUSTENTAVEL');
create type tipo_pergunta as enum ('VERDADEIRO_OU_FALSO', 'PERGUNTA');


create table sala (
    id bigint generated by default as identity not null,
    jogo_iniciou boolean not null,
    jogo_terminou boolean not null,
    privado boolean not null,
    nome varchar(15) not null,
    numero_rodadas int not null,
    numero_jogadores int not null,
    tempo_rodada int not null,
    web_socket_key text not null,
    senha text
);
alter table sala add constraint pk_sala primary key (id);

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
alter table usuario add constraint fk_usuario_id_sala foreign key (id_sala) references sala;

create table token (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token text not null,
    id_usuario bigint not null,
    is_expired boolean not null
);

alter table token add constraint pk_token primary key (id);
alter table token add constraint fk_id_usuario foreign key (id_usuario) references usuario;


create table permissao (
	id bigint generated by default as identity not null,
	nome varchar(100) not null,
	usuario_id BIGINT not null
);
alter table permissao add constraint pk_permissao primary key (id);
alter table permissao add constraint uk_permissao unique (nome, usuario_id);
alter table permissao add constraint fk_permissao_usuario foreign key (usuario_id) references usuario;


create table tema (
    id bigint generated by default as identity not null,
    tema tema_pergunta not null
);
alter table tema add constraint pk_tema primary key (id);

create table pergunta (
    id bigint generated by default as identity not null,
    link text not null,
    questao text not null,
    resposta resposta not null,
    id_tema bigint not null,
    tipo tipo_pergunta not null
);
alter table pergunta add constraint pk_pergunta primary key (id);
alter table pergunta add constraint fk_pergunta foreign key (id_tema) references tema;

create table sala_tema (
    id_sala bigint not null,
    id_tema bigint not null
);
alter table sala_tema add constraint fk_sala_tema_id_sala foreign key (id_sala) references sala;
alter table sala_tema add constraint fk_sala_tema_id_tema foreign key (id_tema) references tema;

create table resposta_pergunta (
    id bigint generated by default as identity not null,
    id_sala bigint not null,
    nome varchar(100) not null
);
alter table resposta_pergunta add constraint pk_resposta_pergunta primary key (id);
alter table resposta_pergunta add constraint fk_resposta_pergunta_id_sala foreign key (id_sala) references sala;

create table resposta_avancar (
    id bigint generated by default as identity not null,
    id_sala bigint not null,
    nome varchar(100) not null
);
alter table resposta_avancar add constraint pk_resposta_avancar primary key (id);
alter table resposta_avancar add constraint fk_resposta_avancar_id_sala foreign key (id_sala) references sala;

create table placar (
    id bigint generated by default as identity not null,
    id_sala bigint not null,
    nome varchar(100) not null,
    pontos int not null default 0
);
alter table placar add constraint pl_placar primary key (id);
alter table placar add constraint fk_placar_id_sala foreign key (id_sala) references sala;

create table sala_pergunta (
    id_sala bigint not null,
    id_pergunta bigint not null
);
alter table sala_pergunta add constraint fk_sala_pergunta_id_sala foreign key (id_sala) references sala;
alter table sala_pergunta add constraint fk_sala_pergunta_id_pergunta foreign key (id_pergunta) references pergunta;





