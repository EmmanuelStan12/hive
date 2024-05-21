export type INavLink = {
    imgURL: string;
    route: string;
    label: string;
};

export type IUpdateUser = {
    userId: string;
    name: string;
    bio: string;
    imageId: string;
    imageUrl: URL | string;
    file: File[];
};

export type INewPost = {
    caption: string;
    file: File[];
    location?: string;
    tags?: string;
};

export type IUpdatePost = {
    postId: string;
    caption: string;
    file?: File[];
    location?: string;
    tags?: string;
};

export type IPost = {
    id: string;
    caption: string;
    imageId: string;
    imageUrl: URL;
    location?: string;
    tags: string[];
    creator: IUser;
    createdAt: string
    likes: []
};

export type IUser = {
    id: string;
    name: string;
    username: string;
    email: string;
    imageUrl: string;
    bio: string;
};

export type INewUser = {
    name: string;
    email: string;
    username: string;
    password: string;
};
