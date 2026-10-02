import math
import os
import struct
import zlib

SCALE = 4
WIDTH = 64 * SCALE
HEIGHT = 32 * SCALE
ROOT = os.path.join(
    os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
    "src",
    "main",
    "resources",
    "assets",
    "ascendant",
    "textures",
    "models",
    "armor",
)


def write_png(path, pixels):
    def chunk(tag, data):
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    raw = b"".join(b"\x00" + pixels[y * WIDTH * 4 : (y + 1) * WIDTH * 4] for y in range(HEIGHT))
    png = (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", WIDTH, HEIGHT, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(raw, 9))
        + chunk(b"IEND", b"")
    )
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as file:
        file.write(png)


def blank():
    return bytearray(WIDTH * HEIGHT * 4)


def hash2(x, y):
    n = (int(x) * 374761393 + int(y) * 668265263) & 0xFFFFFFFF
    n = ((n ^ (n >> 13)) * 1274126177) & 0xFFFFFFFF
    return (n ^ (n >> 16)) & 255


def clamp(value):
    return max(0, min(255, int(value)))


def tint(color, delta):
    return (clamp(color[0] + delta), clamp(color[1] + delta), clamp(color[2] + delta), color[3])


def lighten(color, factor):
    return tuple(clamp(channel * factor) for channel in color[:3]) + (color[3],)


def set_px(buf, x, y, color):
    if color is None or not (0 <= x < WIDTH and 0 <= y < HEIGHT):
        return
    index = (y * WIDTH + x) * 4
    buf[index : index + 4] = bytes(color)


def paint(buf, x, y, w, h, color_at, factor):
    for yy in range(h):
        for xx in range(w):
            color = color_at(xx, yy, w, h)
            if color is not None:
                color = lighten(color, factor)
            set_px(buf, x + xx, y + yy, color)


def body(buf, front, back, side, shoulder, hem):
    s = SCALE
    paint(buf, 20 * s, 20 * s, 8 * s, 12 * s, front, 1.0)
    paint(buf, 32 * s, 20 * s, 8 * s, 12 * s, back, 0.82)
    paint(buf, 16 * s, 20 * s, 4 * s, 12 * s, side, 0.88)
    paint(buf, 28 * s, 20 * s, 4 * s, 12 * s, side, 0.88)
    paint(buf, 20 * s, 16 * s, 8 * s, 4 * s, shoulder, 1.08)
    paint(buf, 28 * s, 16 * s, 8 * s, 4 * s, hem, 0.62)


def arm(buf, sleeve, shoulder, wrist):
    s = SCALE
    paint(buf, 44 * s, 20 * s, 4 * s, 12 * s, sleeve, 1.0)
    paint(buf, 52 * s, 20 * s, 4 * s, 12 * s, sleeve, 0.86)
    paint(buf, 40 * s, 20 * s, 4 * s, 12 * s, sleeve, 0.9)
    paint(buf, 48 * s, 20 * s, 4 * s, 12 * s, sleeve, 0.9)
    paint(buf, 44 * s, 16 * s, 4 * s, 4 * s, shoulder, 1.08)
    paint(buf, 48 * s, 16 * s, 4 * s, 4 * s, wrist, 0.7)


def grain(x, y, amount):
    return (hash2(x, y) - 128) * amount // 128


def stitch(x, y, along_x):
    if along_x:
        return y % 4 == 0 and x % 2 == 0
    return x % 4 == 0 and y % 2 == 0


LIGHT = (132, 94, 56, 255)
LIGHT_PALE = (156, 116, 72, 255)
LIGHT_DARK = (84, 56, 34, 255)
LIGHT_BELT = (58, 38, 24, 255)
BUCKLE = (146, 118, 72, 255)

RIVET_LEATHER = (64, 44, 32, 255)
RIVET_DARK = (40, 28, 20, 255)
METAL = (158, 154, 146, 255)
METAL_DARK = (86, 84, 78, 255)

MAIL_GAP = (48, 52, 58, 255)
MAIL_RING = (124, 130, 138, 255)
MAIL_EDGE = (168, 174, 180, 255)
MAIL_BELT = (86, 58, 38, 255)

WOOL = (42, 36, 60, 255)
WOOL_PANEL = (64, 56, 88, 255)
WOOL_DARK = (26, 22, 40, 255)
CORD = (128, 100, 58, 255)

LINEN = (196, 186, 160, 255)
LINEN_LIGHT = (214, 206, 182, 255)
LINEN_SHADE = (158, 146, 124, 255)
LINEN_CORD = (132, 104, 64, 255)


def leather_surface(x, y, base, dark):
    color = tint(base, grain(x, y, 18))
    if hash2(x // 3, y // 5) > 236:
        color = tint(color, 16)
    return color


def light_cloth(x, y, w, h, short_sleeve=False):
    vy = y / SCALE
    if short_sleeve and vy >= 6.2:
        return None
    if short_sleeve and vy >= 5.5:
        return LIGHT_DARK
    if vy >= 9.6:
        return tint(LIGHT_BELT, grain(x, y, 10))
    if 9.1 <= vy < 9.6 and abs(x - w / 2) < 3:
        return BUCKLE
    edge = x < 2 or x >= w - 2 or y < 2
    base = LIGHT_DARK if edge else (LIGHT_PALE if vy < 4 else LIGHT)
    color = leather_surface(x, y, base, LIGHT_DARK)
    center = abs(x - w / 2)
    if center < 1.2 and int(vy * 2) % 2 == 0 and vy < 9:
        return LIGHT_DARK
    if 2.2 < center < 3.4 and y % 5 == 0:
        return LIGHT_DARK
    return color


def light_side(x, y, w, h):
    if y / SCALE >= 9.6:
        return LIGHT_BELT
    return leather_surface(x, y, LIGHT_DARK if x < 2 else LIGHT, LIGHT_DARK)


def rivet_here(x, y):
    step = 7
    if y % step > 2 or x % step > 2:
        return None
    local_x = x % step
    local_y = y % step
    if local_x == 1 and local_y == 1:
        return METAL
    if local_x <= 2 and local_y <= 2:
        return METAL_DARK
    return None


def rivet_cloth(x, y, w, h):
    vy = y / SCALE
    if vy >= 10.2:
        return tint(RIVET_DARK, grain(x, y, 8))
    if vy >= 9.4:
        return tint((110, 78, 52, 255), grain(x, y, 8))
    rivet = rivet_here(x + 2, y + 3)
    if rivet is not None and 3 < x < w - 3 and 4 < y < h - 8:
        return rivet
    edge = x < 2 or x >= w - 2
    base = RIVET_DARK if edge else RIVET_LEATHER
    return leather_surface(x, y, base, RIVET_DARK)


def mail_color(x, y):
    cell = 4
    px = x % cell
    py = y % cell
    dx = abs(px - 1.5)
    dy = abs(py - 1.5)
    distance = dx + dy
    if distance < 0.7:
        return MAIL_GAP
    if distance < 1.8:
        return MAIL_EDGE if px + py < 2 else MAIL_RING
    return MAIL_GAP


def mail_cloth(x, y, w, h, sleeve=False):
    vy = y / SCALE
    if not sleeve and vy >= 10.3:
        return tint(RIVET_DARK, grain(x, y, 6))
    if not sleeve and vy >= 9.5:
        return tint(MAIL_BELT, grain(x, y, 8))
    if vy <= 0.8 and not sleeve:
        return tint(MAIL_GAP, -8)
    if sleeve and vy >= 11.1:
        return tint(MAIL_GAP, -10)
    return tint(mail_color(x, y), grain(x, y, 8))


def cloth_fold(x, y, w, h, base, panel, dark, cord, collar=False):
    vy = y / SCALE
    vx = x / SCALE
    if collar and vy <= 1.3:
        return tint(dark, grain(x, y, 8))
    if 7.5 <= vy <= 8.3:
        if int(x) % 3 == 0:
            return dark
        return tint(cord, grain(x, y, 8))
    if vy >= 10.4:
        return tint(dark, grain(x, y, 10))
    fold = math.sin(vx * 1.7) * 14 + math.sin(vy * 0.8) * 6
    span = w / SCALE
    in_panel = span > 6 and 2.2 <= vx <= span - 2.2
    color = panel if in_panel else base
    color = tint(color, int(fold) + grain(x, y, 10))
    if abs(vx - span / 2) < 0.35 and y % 3 == 0:
        return tint(dark, -4)
    return color


def sleeve_cloth(base, cuff):
    def color(x, y, w, h):
        if y / SCALE >= 10.2:
            return tint(cuff, grain(x, y, 8))
        fold = math.sin((x / SCALE) * 2.2) * 10
        return tint(base, int(fold) + grain(x, y, 10))

    return color


def build(front, back, side, shoulder, hem, sleeve, arm_top, wrist):
    buf = blank()
    body(buf, front, back, side, shoulder, hem)
    arm(buf, sleeve, arm_top, wrist)
    return buf


def main():
    pieces = {
        "light_leather": build(
            lambda x, y, w, h: light_cloth(x, y, w, h),
            lambda x, y, w, h: light_cloth(x, y, w, h),
            light_side,
            lambda x, y, w, h: leather_surface(x, y, LIGHT, LIGHT_DARK),
            lambda x, y, w, h: LIGHT_DARK,
            lambda x, y, w, h: light_cloth(x, y, w, h, short_sleeve=True),
            lambda x, y, w, h: leather_surface(x, y, LIGHT, LIGHT_DARK),
            lambda x, y, w, h: None,
        ),
        "riveted_leather": build(
            rivet_cloth,
            rivet_cloth,
            rivet_cloth,
            lambda x, y, w, h: leather_surface(x, y, RIVET_LEATHER, RIVET_DARK),
            lambda x, y, w, h: RIVET_DARK,
            rivet_cloth,
            lambda x, y, w, h: leather_surface(x, y, RIVET_LEATHER, RIVET_DARK),
            lambda x, y, w, h: RIVET_DARK,
        ),
        "iron_mail": build(
            lambda x, y, w, h: mail_cloth(x, y, w, h),
            lambda x, y, w, h: mail_cloth(x, y, w, h),
            lambda x, y, w, h: mail_cloth(x, y, w, h),
            lambda x, y, w, h: mail_color(x, y),
            lambda x, y, w, h: MAIL_BELT,
            lambda x, y, w, h: mail_cloth(x, y, w, h, sleeve=True),
            lambda x, y, w, h: mail_color(x, y),
            lambda x, y, w, h: MAIL_GAP,
        ),
        "caster_robe": build(
            lambda x, y, w, h: cloth_fold(x, y, w, h, WOOL, WOOL_PANEL, WOOL_DARK, CORD),
            lambda x, y, w, h: cloth_fold(x, y, w, h, WOOL, WOOL, WOOL_DARK, CORD),
            lambda x, y, w, h: cloth_fold(x, y, w, h, WOOL, WOOL, WOOL_DARK, CORD),
            lambda x, y, w, h: tint(WOOL, grain(x, y, 8)),
            lambda x, y, w, h: WOOL_DARK,
            sleeve_cloth(WOOL, WOOL_DARK),
            lambda x, y, w, h: tint(WOOL, grain(x, y, 8)),
            lambda x, y, w, h: WOOL_DARK,
        ),
        "healer_robe": build(
            lambda x, y, w, h: cloth_fold(x, y, w, h, LINEN, LINEN_LIGHT, LINEN_SHADE, LINEN_CORD, collar=True),
            lambda x, y, w, h: cloth_fold(x, y, w, h, LINEN, LINEN, LINEN_SHADE, LINEN_CORD),
            lambda x, y, w, h: cloth_fold(x, y, w, h, LINEN, LINEN, LINEN_SHADE, LINEN_CORD),
            lambda x, y, w, h: tint(LINEN, grain(x, y, 8)),
            lambda x, y, w, h: LINEN_SHADE,
            sleeve_cloth(LINEN, LINEN_SHADE),
            lambda x, y, w, h: tint(LINEN, grain(x, y, 8)),
            lambda x, y, w, h: LINEN_SHADE,
        ),
    }
    empty = blank()
    for name, pixels in pieces.items():
        path = os.path.join(ROOT, name + "_layer_1.png")
        write_png(path, pixels)
        write_png(os.path.join(ROOT, name + "_layer_2.png"), empty)
        print(name, os.path.getsize(path))


if __name__ == "__main__":
    main()
