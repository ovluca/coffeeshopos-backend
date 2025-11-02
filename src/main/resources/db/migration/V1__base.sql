-- tenants / users
create table shops (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  created_at timestamptz default now()
);

create table users (
  id uuid primary key default gen_random_uuid(),
  firebase_uid text unique not null,
  email text not null,
  role text not null check (role in ('owner','manager','cashier','barista')),
  created_at timestamptz default now()
);

create table user_shops (
  user_id uuid references users(id) on delete cascade,
  shop_id uuid references shops(id) on delete cascade,
  primary key (user_id, shop_id)
);

-- menu
create table categories (
  id uuid primary key default gen_random_uuid(),
  shop_id uuid not null references shops(id) on delete cascade,
  name text not null, position int default 0
);

create table products (
  id uuid primary key default gen_random_uuid(),
  shop_id uuid not null references shops(id) on delete cascade,
  category_id uuid references categories(id),
  name text not null, description text, active boolean default true
);

create table product_variants (
  id uuid primary key default gen_random_uuid(),
  product_id uuid not null references products(id) on delete cascade,
  name text not null, price_cents int not null
);

create table modifiers (
  id uuid primary key default gen_random_uuid(),
  shop_id uuid not null references shops(id) on delete cascade,
  name text not null, price_delta_cents int default 0
);

-- orders
create table orders (
  id uuid primary key default gen_random_uuid(),
  shop_id uuid not null references shops(id) on delete cascade,
  created_by uuid references users(id),
  status text not null check (status in ('OPEN','PAID','CANCELLED')),
  total_cents int not null default 0,
  created_at timestamptz default now()
);

create table order_items (
  id uuid primary key default gen_random_uuid(),
  order_id uuid not null references orders(id) on delete cascade,
  product_variant_id uuid not null references product_variants(id),
  qty int not null, price_cents int not null
);

create table payments (
  id uuid primary key default gen_random_uuid(),
  order_id uuid not null references orders(id) on delete cascade,
  method text not null, amount_cents int not null, created_at timestamptz default now()
);
