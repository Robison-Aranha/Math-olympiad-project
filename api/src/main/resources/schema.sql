create type CLA as enum("CATADORES", "HIGIENIZADORES")
create type ITEM_TIPOS as enum("VIDA", "FORCA", "NIVEL", "DESTREZA", "ARMA", "DEFESA", "ARMADURA")

create table usuario (
	id bigint generated by default as identity not null,
	nome varchar(100) not null,
	dinheiro numeric not null,
	senha varchar(100) not null,
	ativo BOOLEAN not null
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

create table classe (

    id bigint generated by default as identity not null,
    imagem text not null,
    forca int not null,
    fama int not null,
    destreza int not null,
    vida int not null,
    nivel int not null,
    descricao text not null,
);
alter table classe add constraint pk_classe primary key (id);

create table quest (
    id bigint generated by default as identity not null,
    tempo Numeric not null,
    imagem text not null,
    descricao text not null
)

alter table quest add constraint pk_quest primary key (id);

create table personagem (

    id bigint generated by default as identity not null,
    nome varchar(100) not null,
    cla CLA not null,
    id_quest bigint,
    id_classe bigint not null,
    termino_ultima_quest timestamp,
);
alter table personagem add constraint pk_personagem primary key (id);
alter table personagem add constraint fk_personagem_id_classe foreign key (id_classe) references classe;
alter table personagem add constraint fk_personagem_id_quest foreign key (id_quest) references quest;

create table usuario_personagem (
    id_usuario bigint not null,
    id_personagem bigint not null
);
alter table usuario_personagem add constraint fk_usuario_personagem_id_usuario foreign key (id_usuario) references usuario;
alter table usuario_personagem add constraint fk_usuario_personagem_id_personagem foreign key (id_personagem) references personagem;


create table item (
    id bigint generated by default as identity not null,
    imagem text not null,
    preco numeric not null,
    nome varchar(100) not null,
    tipo ITEM_TIPOS not null,
    valor int not null,
    descricao text not null
);
alter table item add constraint pk_item primary key (id);


create table usuario_item (
    id_usuario bigint not null,
    id_item bigint not null
);

alter table usuario_item add constraint fk_usuario_item_id_usuario foreign key (id_usuario) references usuario;
alter table usuario_item add constraint fk_usuario_item_id_item foreign key (id_item) references item;


create table quest_item (
    id_quest bigint not null,
    id_item bigint not null
)

alter table quest_item add constraint foreign key (id_quest) references quest;
alter table quest_item add constraint foreign key (id_item) references item;

